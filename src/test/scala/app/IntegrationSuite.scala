package app

import munit.FunSuite
import upickle.default.{write, read}
import io.undertow.Undertow
import startup.ast.{ExpressionToSerialize, Expression, Plus, Mult, Num}
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

  test(
    "Serialize and deserialize a validated valid expression with integers processed through http server"
  ) {
    assert(
      this.withServer("MinimalApplication", RoutesMain)(
        { host =>

          // preparing expression to process
          val IntExpression: Expression[Int] =
            Mult(Plus(Num(7), Num(10)), Num(11))
          val ValidIntExpression: Either[List[String], Expression[Int]] =
            Expression.validateExpression(IntExpression)
          val ValidIntExpressionToSerialize =
            ExpressionToSerialize(ValidIntExpression)
          val ValidIntExpressionJson =
            write[ExpressionToSerialize[Int]](ValidIntExpressionToSerialize)

          // Sending expression to server for evaluation and further processing
          val test66 = requests.post(
            s"$host/evaluate1",
            data = ValidIntExpressionJson,
            connectTimeout = 2000000,
            readTimeout = 2000000
          )

          // Retrieving and reading the processed expression from server
          val stp0 = 0
          val parsedJson = ujson.read(test66.text())
          val IntExpressionJsonReceived: String = parsedJson("result").str
          val stp1 = 0
          val eitherExpression: Either[List[String], Expression[Int]] =
            read[Either[List[String], Expression[Int]]](
              IntExpressionJsonReceived
            )
          val dfExpressionEvaluated =
            eitherExpression match
              case Right(expression) =>
                val result1 = Expression.validateExpression(expression)
                val result =
                  Expression.evaluateValidExpression(result1).right.get
                result
              case Left(error) => throw new Exception(s"Invalid input: $error")

          // Asserting test validity
          val stp = 0
          dfExpressionEvaluated == 187
        }
      )
    )
  }

  test(
    "Serialize and deserialize a validated non-valid operation with integers processed through http server"
  ) {
    assert(
      this.withServer("MinimalApplication", RoutesMain)(
        { host =>

          // preparing expression to process
          val IntExpression: Expression[Int] =
            Mult(Plus(Num(7), Num(10)), Num(5500))
          val ValidIntExpression: Either[List[String], Expression[Int]] =
            Expression.validateExpression(IntExpression)
          val ValidIntExpressionToSerialize =
            ExpressionToSerialize(ValidIntExpression)
          val ValidIntExpressionJson =
            write[ExpressionToSerialize[Int]](ValidIntExpressionToSerialize)
          val stp00 = 0
          // Sending expression to server for evaluation and further processing
          val test66 = requests.post(
            s"$host/evaluate1",
            data = ValidIntExpressionJson,
            connectTimeout = 2000000,
            readTimeout = 2000000
          )

          // Retrieving and reading the processed expression from server
          val stp0 = 0
          val parsedJson = ujson.read(test66.text())
          val IntExpressionJsonReceived: String = parsedJson("result").str
          val eitherExpression: Either[List[String], Expression[Dataset[Row]]] =
            read[Either[List[String], Expression[Dataset[Row]]]](
              IntExpressionJsonReceived
            )

          // Asserting test validity
          val stp1 = 0
          eitherExpression.left.get.length == 1
        }
      )
    )
  }

  test(
    "Serialize and deserialize a validated non-valid expression with integers processed through http server"
  ) {
    assert(
      this.withServer("MinimalApplication", RoutesMain)(
        { host =>

          // preparing expression to process
          val IntExpression: Expression[Int] =
            Mult(Plus(Num(3), Num(3)), Num(5500))
          val ValidIntExpression: Either[List[String], Expression[Int]] =
            Expression.validateExpression(IntExpression)
          val ValidIntExpressionToSerialize =
            ExpressionToSerialize(ValidIntExpression)
          val ValidIntExpressionJson =
            write[ExpressionToSerialize[Int]](ValidIntExpressionToSerialize)
          val stp00 = 0
          // Sending expression to server for evaluation and further processing
          val test66 = requests.post(
            s"$host/evaluate1",
            data = ValidIntExpressionJson,
            connectTimeout = 2000000,
            readTimeout = 2000000
          )

          // Retrieving and reading the processed expression from server
          val stp0 = 0
          val parsedJson = ujson.read(test66.text())
          val IntExpressionJsonReceived: String = parsedJson("error").str
          val eitherExpression: Either[List[String], Expression[Dataset[Row]]] =
            read[Either[List[String], Expression[Dataset[Row]]]](
              IntExpressionJsonReceived
            )

          // Asserting test validity
          val stp1 = 0
          eitherExpression.left.get.length == 2
        }
      )
    )
  }

  test(
    "Serialize and deserialize a validated valid expression with dataframe processed through http server"
  ) {
    assert(
      this.withServer("MinimalApplication", RoutesMain)(
        { host =>

          // preparing expression to process
          val dfExpressionToProcess: Expression[Dataset[Row]] =
            ExpressionsExemples.dfExpression0
          val dfExpressionToProcessValidated
              : Either[List[String], Expression[Dataset[Row]]] =
            Expression.validateExpression(dfExpressionToProcess)
          val dfExpressionToSerialize: ExpressionToSerialize[Dataset[Row]] =
            ExpressionToSerialize(dfExpressionToProcessValidated)
          val dfExpressionJson: String =
            write[ExpressionToSerialize[Dataset[Row]]](dfExpressionToSerialize)

          // Sending expression to server for evaluation and further processing
          val test77 = requests.post(
            s"$host/evaluate2",
            data = dfExpressionJson,
            connectTimeout = 20000,
            readTimeout = 20000
          )

          // Retrieving and reading the processed expression from server
          val parsedJson = ujson.read(test77.text())
          val dfExpressionJsonReceived: String = parsedJson("result").str
          val eitherExpression: Either[List[String], Expression[Dataset[Row]]] =
            read[Either[List[String], Expression[Dataset[Row]]]](
              dfExpressionJsonReceived
            )
          val dfExpressionEvaluated =
            eitherExpression match
              case Right(expression) =>
                val result1 = Expression.validateExpression(expression)
                val result =
                  Expression.evaluateValidExpression(result1).right.get
                result
              case Left(error) => throw new Exception(s"Invalid input: $error")

          // Asserting test validity
          dfExpressionEvaluated.schema == DataFramesExemples.schema1
          dfExpressionEvaluated.count() == DataFramesExemples.data1.length
        }
      )
    )
  }

  test(
    "Serialize and deserialize a validated non-valid expression with dataframe processed through http server"
  ) {
    assert(
      this.withServer("MinimalApplication", RoutesMain)(
        { host =>
          // preparing expression to process
          val dfExpressionToProcess: Expression[Dataset[Row]] =
            ExpressionsExemples.dfExpression1
          val dfExpressionToProcessValidated
              : Either[List[String], Expression[Dataset[Row]]] =
            Expression.validateExpression(dfExpressionToProcess)
          val dfExpressionToSerialize: ExpressionToSerialize[Dataset[Row]] =
            ExpressionToSerialize(dfExpressionToProcessValidated)
          val dfExpressionJson: String =
            write[ExpressionToSerialize[Dataset[Row]]](dfExpressionToSerialize)

          // Sending expression to server for evaluation and further processing
          val test77 = requests.post(
            s"$host/evaluate2",
            data = dfExpressionJson,
            connectTimeout = 20000,
            readTimeout = 20000
          )

          // Retrieving and reading the processed expression from server
          val parsedJson = ujson.read(test77.text())
          val dfExpressionJsonReceived: String = parsedJson("error").str
          val eitherExpression: Either[List[String], Expression[Dataset[Row]]] =
            read[Either[List[String], Expression[Dataset[Row]]]](
              dfExpressionJsonReceived
            )

          // Asserting test validity
          eitherExpression.left.get.length == 3
        }
      )
    )
  }
