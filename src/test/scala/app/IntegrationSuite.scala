package app

import munit.FunSuite
import upickle.default.{write, read}
import io.undertow.Undertow
import startup.ast.{
  //myStart,
  //Arg,
  ExpressionToSerialize,
  Expression,
  Plus,
  Mult,
  Num
}
import startup.ast.DataFrameName.given
import org.apache.spark.sql.{Dataset, Row}
import sparkjobs.{DataFramesExemples, ExpressionsExemples}

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

/*
  test("test2") {
    assert(
      this.withServer("MinimalApplication", RoutesMain)(
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
*/



/*
  test("test6") {
    assert(
      this.withServer("MinimalApplication", RoutesMain)(
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
*/

  test("test66") {
    assert(
      this.withServer("MinimalApplication", RoutesMain)(
        { host =>
          
          // preparing expression to process
          val IntExpression: Expression[Int] =
            Num(3)
            //Mult(Plus(Num(3), Num(4)), Num(5))
          val ValidIntExpression: Either[String, Expression[Int]] =
            Expression.validateExpression(IntExpression)
          val ValidIntExpressionToSerialize = ExpressionToSerialize(ValidIntExpression)
          val ValidIntExpressionJson = write(ValidIntExpressionToSerialize)
          
          // Sending expression to server for evaluation and further processing
          val test66 = requests.post(
            s"$host/jsonastvalid",
            data = ValidIntExpressionJson,
            connectTimeout = 20000,
            readTimeout = 20000
          )
          
          // Retrieving and reading the processed expression from server
          // Asserting test validity
          test66.text().toInt == 3//5
        }
      )
    )
  }

/*
  test(
    "test7: Serialize and deserialize expression with dataframe processed through http server"
  ) {
    assert(
      this.withServer("MinimalApplication", RoutesMain)(
        { host =>

          // preparing expression to process
          val dfExpressionToSerialize: ExpressionToSerialize[Dataset[Row]] =
            ExpressionToSerialize(ExpressionsExemples.dfExpression0)
          val dfExpressionJson: String = write(dfExpressionToSerialize)

          // Sending expression to server for evaluation and further processing
          val test7 = requests.post(
            s"$host/jsonastdf",
            data = dfExpressionJson,
            connectTimeout = 20000,
            readTimeout = 20000
          )

          // Retrieving and reading the processed expression from server
          val dfExpressionJsonReceived: String = ujson.read(test7.text()).str
          val dfExpressionRead: Expression[Dataset[Row]] =
            read[Expression[Dataset[Row]]](dfExpressionJsonReceived)
          val dfExpressionEvaluated: Dataset[Row] =
            Expression.evaluate(dfExpressionRead)

          // Asserting test validity
          dfExpressionEvaluated.show()
          dfExpressionEvaluated.schema == DataFramesExemples.schema
          dfExpressionEvaluated.count() == DataFramesExemples.data.length
        }
      )
    )
  }
*/

  test(
    "test77: Serialize and deserialize validated expression with dataframe processed through http server"
  ) {
    assert(
      this.withServer("MinimalApplication", RoutesMain)(
        { host =>

          // preparing expression to process
          val dfExpressionToProcess: Expression[Dataset[Row]] =
            ExpressionsExemples.dfExpression0
          val dfExpressionToProcessValidated: Either[String, Expression[Dataset[Row]]] =
            Expression.validateExpression(dfExpressionToProcess)
          val dfExpressionToSerialize: ExpressionToSerialize[Dataset[Row]] =
            ExpressionToSerialize(dfExpressionToProcessValidated)
          val dfExpressionJson: String = write(dfExpressionToSerialize)

          // Sending expression to server for evaluation and further processing
          val test77 = requests.post(
            s"$host/jsonastdfvalid",
            data = dfExpressionJson,
            connectTimeout = 20000,
            readTimeout = 20000
          )

          // Retrieving and reading the processed expression from server
          val dfExpressionJsonReceived: String = ujson.read(test77.text()).str
          
          val eitherExpression: Either[String, Expression[Dataset[Row]]] =
            read[Either[String, Expression[Dataset[Row]]]](dfExpressionJsonReceived)
          
          val dfExpressionEvaluated = 
            eitherExpression match
              case Right(expression) =>
                val result: Dataset[Row] = Expression.evaluate(expression)
                result
              case Left(error) => throw new Exception(s"Invalid input: $error")

          // Asserting test validity
          dfExpressionEvaluated.show()
          dfExpressionEvaluated.schema == DataFramesExemples.schema1
          dfExpressionEvaluated.count() == DataFramesExemples.data1.length
        }
      )
    )
  }
