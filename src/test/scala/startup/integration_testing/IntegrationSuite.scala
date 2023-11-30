package startup.integration_testing

import munit.FunSuite
import upickle.default.{write, read}
import io.undertow.Undertow
import startup.ast.{
  myStart,
  Arg,
  ExpressionToSerialize,
  Expression,
  Plus,
  Mult,
  Num
}
import startup.dataframe.{dfExpression, schema, data}
import startup.ast.DataFrameName.given
import org.apache.spark.sql.{Dataset, Row}

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
          val myDataInstance = myStart(myint)
          val argInstance = Arg(myDataInstance)
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

/*
//  test("test4: issue with server closing the connection too early") {
//    assert(
//      this.withServer("MinimalApplication", app.MinimalRoutesMain)(
//        { host =>
//          requests.delete(s"$host/do-thing", check = false).statusCode == 405
//        }
//      )
//    )
//  }
*/

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


  test("test6") {
    assert(
      this.withServer("MinimalApplication", app.MinimalRoutesMain)(
        { host =>
          
          // preparing expression to process
          val IntExpression: Expression[Int] =
            Mult(Plus(Num(3), Num(4)), Num(5))
          val IntExpressionToSerialize = ExpressionToSerialize(IntExpression)
          val IntExpressionJson = write(IntExpressionToSerialize)
          
          // Sending expression to server for evaluation and further processing
          val test6 = requests.post(
            s"$host/jsonast",
            data = IntExpressionJson,
            connectTimeout = 20000,
            readTimeout = 20000
          )
          
          // Retrieving and reading the processed expression from server
          // Asserting test validity
          test6.text().toInt == 35
        }
      )
    )
  }

  test(
    "test7: Serialize and deserialize expression with dataframe processed through http server"
  ) {
    assert(
      this.withServer("MinimalApplication", app.MinimalRoutesMain)(
        { host =>

          // preparing expression to process
          val dfExpressionToSerialize: ExpressionToSerialize[Dataset[Row]] =
            ExpressionToSerialize(dfExpression)
          val dfExpressionJson: String = write(dfExpressionToSerialize)

          // Sending expression to server for evaluation and further processing
          val test7 = requests.post(
            s"$host/jsonastdf",
            data = dfExpressionJson,
            connectTimeout = 80000000,
            readTimeout = 80000000
          )

          // Retrieving and reading the processed expression from server
          val dfExpressionJsonReceived: String = ujson.read(test7.text()).str
          val dfExpressionRead: Expression[Dataset[Row]] =
            read[Expression[Dataset[Row]]](dfExpressionJsonReceived)
          val dfExpressionEvaluated: Dataset[Row] =
            Expression.evaluate(dfExpressionRead)

          // Asserting test validity
          dfExpressionEvaluated.show()
          dfExpressionEvaluated.schema == schema
          dfExpressionEvaluated.count() == data.length
        }
      )
    )
  }
