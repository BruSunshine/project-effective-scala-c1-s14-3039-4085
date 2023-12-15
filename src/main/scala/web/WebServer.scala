// The web package contains the server-side logic of the application.
package web

// Importing necessary libraries and modules.
import startup.ast.{Expression, Num}
import startup.ast.DataFrameName.given
import startup.ast.Expression.given
import sparkjobs.{SparkJob, DataFramesExemples, dfToString}
import org.apache.spark.sql.{Dataset, Row}
import org.apache.spark.sql.functions._
import cask.model.Request
import io.undertow.util.StatusCodes
import upickle.default.{read, write}
import concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Await}
import scala.concurrent.duration._
import app.SparkMain.sparkSession
import scala.util.{Failure, Success, Try}

// Object to hold the results of the evaluated expressions.
object ResultsHolder:
  var shareintExpressionEvaluated: Either[List[String], Option[Int]] = Right(
    None
  )
  var sharedfExpressionEvaluated: Either[List[String], Option[String]] = Right(
    None
  )

// Object defining test routes for the web server.
object TestRoutes extends cask.Routes:

  // Route to get the result of the AST evaluation on numbers.
  @cask.get("/resultsstring1")
  def getResultNumbers() =
    // s"""|This is the result the ast evaluation
    //    |on numbers ${ResultsHolder.shareintExpressionEvaluated.right.get}""".stripMargin
    ResultsHolder.shareintExpressionEvaluated match
      case Right(result) =>
        s"""|This is a valid result of the ast evaluation 
            |on numbers ${result}""".stripMargin
      case Left(result) =>
        s"""|This is an error result of the ast evaluation 
            |on numbers ${result}""".stripMargin

  // Route to get the result of the AST evaluation on dataframes.
  @cask.get("/resultsstring2")
  def getResultDataframe() =
    // s"""|This is the result the ast evaluation
    //    |on dataframes ${ResultsHolder.sharedfExpressionEvaluated.right.get}""".stripMargin
    ResultsHolder.sharedfExpressionEvaluated match
      case Right(result) =>
        s"""|This is a valid result of the ast evaluation 
            |on dataframes ${result}""".stripMargin
      case Left(result) =>
        s"""|This is an error result of the ast evaluation 
            |on dataframes ${result}""".stripMargin

  // Route to show the dataframe.
  @cask.get("/showdfstring")
  def showDf(): cask.Response[String] =
    val sparkJobResult: Future[cask.Response[String]] =
      Future {
        val dfstring: Either[List[String], String] =
          SparkJob.convertDummyDfValidatedToString(
            sparkSession,
            DataFramesExemples.schema1,
            DataFramesExemples.data1
          )
        dfstring match
          case Right(result) =>
            cask.Response(data = result, statusCode = StatusCodes.ACCEPTED)
          case Left(errorMessage) =>
            cask.Response(
              data = s"Error: $errorMessage",
              statusCode = StatusCodes.INTERNAL_SERVER_ERROR
            )
      }
    val finalResponse: cask.Response[String] =
      Await.result(sparkJobResult, 10.seconds)
    finalResponse
  end showDf

  // Route to reverse the text in the request.
  @cask.post("/do-thing")
  def doThing(request: cask.Request) =
    request.text().reverse

  initialize()

end TestRoutes

// Object defining the routes for serving static files.
object StaticFiles extends cask.Routes:

  // Route to serve static files from the "/static" path.
  @cask.staticFiles("/static")
  def staticFiles(): String =
    "src/main/resources/static" // The directory containing the static files.

  initialize()

end StaticFiles

// Object defining the routes for handling JSON POST requests.
object JsonPost extends cask.Routes:

  // Route to evaluate an expression of type Int ot Double.
  @cask.postJson("/evaluate1")
  def evaluate1(argast: ujson.Value): ujson.Obj =
    // Function to try to evaluate the expression.
    def tryeval1(argast: ujson.Value): Try[ujson.Obj] =
      Try {
        // Parse the JSON value into an Either.
        val eitherExpression: Either[List[String], Expression[Int]] =
          read[Either[List[String], Expression[Int]]](argast)
        eitherExpression match
          case Right(expression) =>
            // Evaluate the expression.
            val intResultAsExpression = Expression
              .evaluateValidExpression(
                Expression.validateExpression(expression)
              )
              .flatMap(x => Expression.validateExpression(Num(x)))
            // Convert the result to JSON.
            val intResultJson: String = write(intResultAsExpression)
            intResultAsExpression match
              case Right(_) =>
                val result = Expression
                  .evaluateValidExpression(intResultAsExpression)
                  .right
                  .get
                // Store the result in the ResultsHolder.
                ResultsHolder.shareintExpressionEvaluated = Right(Some(result))
              case Left(_) =>
                ResultsHolder.shareintExpressionEvaluated = Right(None)
            // Return the result as a JSON object.
            ujson.Obj("result" -> intResultJson)
          case Left(error) =>
            val dfErrorJson: String = write(eitherExpression)
            // Return the error as a JSON object.
            ujson.Obj("error" -> dfErrorJson)
      }
    end tryeval1
    val result: Try[ujson.Obj] = tryeval1(argast)
    result match
      case Success(value)     => value
      case Failure(exception) =>
        // Return the exception message as a JSON object.
        ujson.Obj("error" -> exception.getMessage)

  // Route to evaluate an expression of type Dataset[Row].
  @cask.postJson("/evaluate2")
  def evaluate2(argast: ujson.Value): ujson.Obj =
    // Function to try to evaluate the expression.
    def tryeval2(argast: ujson.Value): Try[ujson.Obj] =
      Try {
        // Parse the JSON value into an Either.
        val eitherExpression: Either[List[String], Expression[Dataset[Row]]] =
          read[Either[List[String], Expression[Dataset[Row]]]](argast)
        eitherExpression match
          case Right(expression) =>
            // Evaluate the expression.
            val dfResultAsExpression = Expression
              .evaluateValidExpression(
                Expression.validateExpression(expression)
              )
              .flatMap(x => Expression.validateExpression(Num(x)))
            // Convert the result to JSON.
            val dfResultJson: String = write(dfResultAsExpression)

            dfResultAsExpression match
              case Right(_) =>
                val result = Expression
                  .evaluateValidExpression(dfResultAsExpression)
                  .right
                  .get
                  .dfToString
                // Store the result in the ResultsHolder.
                ResultsHolder.sharedfExpressionEvaluated =
                  Right(Some(dfResultJson + result))
              case Left(_) =>
                ResultsHolder.sharedfExpressionEvaluated = Right(None)
            // Return the result as a JSON object.
            ujson.Obj("result" -> dfResultJson)
          case Left(error) =>
            val dfErrorJson: String = write(eitherExpression)
            // Return the error as a JSON object.
            ujson.Obj("error" -> dfErrorJson)
      }
    end tryeval2
    val result: Try[ujson.Obj] = tryeval2(argast)
    result match
      case Success(value)     => value
      case Failure(exception) =>
        // Return the exception message as a JSON object.
        ujson.Obj("error" -> exception.getMessage)

  initialize()

end JsonPost
