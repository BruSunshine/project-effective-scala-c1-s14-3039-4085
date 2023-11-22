package startup.integration_testing

import munit.FunSuite
import upickle.default.write
import io.undertow.Undertow

trait IntegrationSuite extends munit.FunSuite

class httpServerSuite extends IntegrationSuite:
  extension (test: FunSuite)
    def withServer(name: String, example: cask.main.Main)(
        f: String => Boolean
    ): Boolean =
      val server = Undertow.builder
        .addHttpListener(8082, "localhost")
        .setHandler(example.defaultHandler)
        .build
      server.start()
      val res =
        try f("http://localhost:8082")
        finally server.stop()
      res
  test("test1") {
    assert(
      this.withServer("MinimalApplication", app.MinimalRoutesMain)(
        { host =>
          val test1 = requests.get(host)
          test1.text() == "Hello World!evaluation of expression Mult(Plus(Num(1),Num(5)),Num(7)) with parameters 1 and 5 and 7 is 42"
        }
      )
    )
  }

  test("test2") {
    assert(
      this.withServer("MinimalApplication", app.MinimalRoutesMain)(
        { host =>
          val myint: Int = 123
          val myDataInstance = startup.myStart(myint)
          val argInstance = startup.Arg(myDataInstance)
          val jsonArgString = write(argInstance)
          val test2 = requests.post(
            s"$host/json",
            data = jsonArgString,
            connectTimeout = 20000
          )
          test2.text().toString() == "\"OK 5\""
        }
      )
    )
  }

  test("test3") {
    assert(
      this.withServer("MinimalApplication", app.MinimalRoutesMain)(
        { host =>
          val test3 = requests.post(s"$host/do-thing", data = "hello")
          test3.text() == "olleh"
        }
      )
    )
  }

//  test("test4: issue with server closing the connection too early") {
//    assert(
//      this.withServer("MinimalApplication", app.MinimalRoutesMain)(
//        { host =>
//          requests.delete(s"$host/do-thing", check = false).statusCode == 405
//        }
//      )
//    )
//  }

  test("test5") {
    assert(
      this.withServer("MinimalApplication", app.MinimalRoutesMain)(
        { host =>
          val test5 = requests.get(s"$host/doesnt-exist", check = false)
          test5.statusCode == 404
        }
      )
    )
  }
