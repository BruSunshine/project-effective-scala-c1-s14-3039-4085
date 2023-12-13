package web

import io.undertow.Undertow
import app.RoutesMain
import munit.FunSuite

class WebServerSuite extends munit.FunSuite:
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

  test(
    "POST /do-thing should return the expected result"
  ) {
    assert(
      this.withServer("MinimalRoutes", RoutesMain)(
        { host =>
          val test = requests.post(s"$host/do-thing", data = "hello")
          test.statusCode == 200 && test.text() == "olleh"
        }
      )
    )
  }

  test(
    "GET /showdfstring should return the expected result"
  ) {
    assert(
      this.withServer("MinimalRoutes", RoutesMain)(
        { host =>
          val test = requests.get(s"$host/showdfstring", check = false)
          val testtext = test.text()
          val expectedString =
            """1| 1.0| a| 1
              |2| 2.0| b| 2
              |3| 3.0| c| 3
              |4| 4.0| d| 4
              |5| 5.0| e| 5""".stripMargin
          (test.statusCode == 202) & (testtext == expectedString)
        }
      )
    )
  }

  test(
    "GET /doesnt-exist should return correct status code"
  ) {
    assert(
      this.withServer("MinimalRoutes", RoutesMain)(
        { host =>
          val test = requests.get(s"$host/doesnt-exist", check = false)
          test.statusCode == 404
        }
      )
    )
  }

  test(
    "GET /resultsstring1 should return the expected string"
  ) {
    assert(
      this.withServer("MinimalRoutes", RoutesMain)(
        { host =>
          val test = requests.get(s"$host/resultsstring1")
          test.statusCode == 200 && test
            .text() == """|This is the result the ast evaluation 
                          |on numbers None""".stripMargin
        }
      )
    )
  }

  test(
    "GET /resultsstring2 should return the expected string"
  ) {
    assert(
      this.withServer("MinimalRoutes", RoutesMain)(
        { host =>
          val test = requests.get(s"$host/resultsstring2")
          test.statusCode == 200 && test
            .text() == """|This is the result the ast evaluation 
                          |on dataframes None""".stripMargin
        }
      )
    )
  }
  test("Static files are served correctly") {
    assert(
      this.withServer("StaticFiles", RoutesMain)(
        { host =>
          val responseA = requests.get(
            s"$host/static/dataAcquisitionForm.html",
            check = false
          )
          val responseB = requests.get(s"$host/static/toc.html", check = false)
          responseA.statusCode == 200 && responseB.statusCode == 200
        }
      )
    )
  }

  test("POST /evaluate1 with valid JSON should return 200 status code") {
    assert(
      this.withServer("MinimalApplication", RoutesMain)(
        { host =>
          val validJson1 =
            """|{
              |"argast":[
              |  1,
              |  {
              |    "$type":"startup.ast.Mult",
              |    "a":{
              |      "$type":"startup.ast.Num",
              |      "n":9
              |    },
              |    "b":{
              |      "$type":"startup.ast.Plus",
              |      "a":{
              |        "$type":"startup.ast.Num",
              |        "n":9
              |      },
              |      "b":{
              |        "$type":"startup.ast.Num",
              |        "n":2
              |      }
              |    }
              |  }
              |]
              |}""".stripMargin
          val response =
            requests.post(s"$host/evaluate1", data = validJson1, check = false)
          response.statusCode == 200
        }
      )
    )
  }

  test("POST /evaluate1 with invalid JSON should return error") {
    assert(
      this.withServer("MinimalApplication", RoutesMain)(
        { host =>
          val invalidJson1 =
            """|{
              |"argast":[
              |  1,
              |  {
              |    "$type":"startup.ast.Num",
              |    "n":{
              |      "name":3
              |    }
              |  }
              |]
              |}""".stripMargin
          val response = requests.post(
            s"$host/evaluate1",
            data = invalidJson1,
            check = false
          )
          val responseBody = response.text()
          val jsonResponse = ujson.read(responseBody)
          jsonResponse.obj.get("error").isDefined
        }
      )
    )
  }

  test("POST /evaluate2 with valid JSON should return 200 status code") {
    assert(
      this.withServer("MinimalApplication", RoutesMain)(
        { host =>
          val validJson2 =
            """|{
              |"argast":[
              |  1,
              |  {
              |    "$type":"startup.ast.Mult",
              |    "a":{
              |      "$type":"startup.ast.Num",
              |      "n":{"name":"2edaf417c083d1f4bff8e27a55381babf7823727aecc6fb140ab93ff1bfc67d5"}
              |    },
              |    "b":{
              |      "$type":"startup.ast.Plus",
              |      "a":{
              |        "$type":"startup.ast.Num",
              |        "n":{"name":"2edaf417c083d1f4bff8e27a55381babf7823727aecc6fb140ab93ff1bfc67d5"}
              |      },
              |      "b":{
              |        "$type":"startup.ast.Num",
              |        "n":{"name":"2edaf417c083d1f4bff8e27a55381babf7823727aecc6fb140ab93ff1bfc67d5"}
              |      }
              |    }
              |  }
              |]
              |}""".stripMargin
          val response =
            requests.post(s"$host/evaluate2", data = validJson2, check = false)
          response.statusCode == 200
        }
      )
    )
  }

  test("POST /evaluate2 with invalid JSON should return error") {
    assert(
      this.withServer("MinimalApplication", RoutesMain)(
        { host =>
          val invalidJson2 =
            """|{
              |"argast":[
              |  1,
              |  {
              |    "$type":"startup.ast.Num",
              |    "n":{
              |      "name":"dfg"
              |    }
              |  }
              |]
              |}""".stripMargin
          val response = requests.post(
            s"$host/evaluate2",
            data = invalidJson2,
            check = false
          )
          val responseBody = response.text()
          val jsonResponse = ujson.read(responseBody)
          jsonResponse.obj.get("error").isDefined
        }
      )
    )
  }
