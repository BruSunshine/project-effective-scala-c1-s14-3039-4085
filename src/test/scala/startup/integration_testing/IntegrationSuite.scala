package startup.integration_testing

import munit.FunSuite

import io.undertow.Undertow

trait IntegrationSuite extends munit.FunSuite

import upickle.default.{write}
val myint:Int = 123
val myDataInstance = startup.myStart(myint)
val jsonString = write(myDataInstance)


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
  test("MinimalApplication") {
    assert(
      this.withServer("MinimalApplication", app.MinimalRoutesMain)(
        { host =>
          val success = requests.get(host)
          val conditions = List(
            success.text() == "Hello World!evaluation of expression Mult(Plus(Num(1),Num(5)),Num(7)) with parameters 1 and 5 and 7 is 42",
            success.statusCode == 200,
            requests
              .get(s"$host/doesnt-exist", check = false)
              .statusCode == 404,
            requests.post(s"$host/do-thing", data = "hello").text() == "olleh",
            requests.delete(s"$host/do-thing", check = false).statusCode == 405,
            requests.post(s"$host/json", data = jsonString).text() == "OK 42",
          )
          conditions.forall(identity)
        }
      )
    )
  }

/*
trait IntegrationSuite extends munit.FunSuite

class httpServerSuite extends IntegrationSuite:

    val httpServer = startup.HttpServer()

    override def beforeAll(): Unit = httpServer.start(8888)

    override def afterAll(): Unit = httpServer.stop()

    test("server is running") {
        // Perform HTTP request here
    }

    test("server is doing ...") {
        // Perform HTTP request here
    }

end httpServerSuite

 */
