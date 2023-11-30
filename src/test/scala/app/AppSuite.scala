package app
import io.undertow.Undertow

import munit.FunSuite

class AppSuite extends munit.FunSuite:
  extension (test: FunSuite)
    def withServer(name: String, example: cask.main.Main)(
        f: String => Boolean
    ): Boolean =
      val server = Undertow.builder
        .addHttpListener(8081, "localhost")
        .setHandler(example.defaultHandler)
        .build
      server.start()
      val res =
        try f("http://localhost:8081")
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
            requests.delete(s"$host/do-thing", check = false).statusCode == 405
          )
          conditions.forall(identity)
        }
      )
    )
  }
