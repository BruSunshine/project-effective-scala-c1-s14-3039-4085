package web

import startup.ast.{Expression, Num}
import startup.ast.DataFrameName.given
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

object ResultsHolder:
  var shareintExpressionEvaluated: Option[Int] = None
  var sharedfExpressionEvaluated: Option[String] = None

object MinimalRoutes extends cask.Routes:

  @cask.get("/resultsstring1")
  def getResultNumbers() =
    s"""|This is the result the ast evaluation 
        |on numbers ${ResultsHolder.shareintExpressionEvaluated}""".stripMargin

  @cask.get("/resultsstring2")
  def getResultDataframe() =
    s"""|This is the result the ast evaluation 
        |on dataframes ${ResultsHolder.sharedfExpressionEvaluated}""".stripMargin

  @cask.get("/showdfstring")
  def showDf(): cask.Response[String] =
    val sparkJobResult: Future[cask.Response[String]] =
      Future {
        val dfstring: Either[String, String] =
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

  @cask.post("/do-thing")
  def doThing(request: cask.Request) =
    request.text().reverse

  initialize()

end MinimalRoutes

object StaticFiles extends cask.Routes:

  @cask.staticFiles("/static")
  def staticFiles(): String =
    "src/main/resources/static"

  initialize()

end StaticFiles

object JsonPost extends cask.Routes:

  @cask.postJson("/evaluate1")
  def evaluate1(argast: ujson.Value): ujson.Obj =
    def eval1(argast: ujson.Value): Try[ujson.Obj] =
      Try {
        val eitherExpression: Either[String, Expression[Int]] =
          read[Either[String, Expression[Int]]](argast)
        eitherExpression match
          case Right(expression) =>
            val intExpressionEvaluated: Int = Expression.evaluate(expression)
            ResultsHolder.shareintExpressionEvaluated =
              Some(intExpressionEvaluated)
            ujson.Obj("result" -> intExpressionEvaluated)
          case Left(error) =>
            ujson.Obj("error" -> s"Invalid input: $error")
      }
    val result: Try[ujson.Obj] = eval1(argast)
    result match
      case Success(value) =>
        ujson.Obj("result" -> value.toString)
      case Failure(exception) =>
        ujson.Obj("error" -> exception.getMessage)

  @cask.postJson("/evaluate2")
  def evaluate2(argast: ujson.Value): ujson.Obj =
    def eval2(argast: ujson.Value): Try[ujson.Obj] =
      Try {
        val eitherExpression: Either[String, Expression[Dataset[Row]]] =
          read[Either[String, Expression[Dataset[Row]]]](argast)
        eitherExpression match
          case Right(expression) =>
            val dfExpressionEvaluated: Dataset[Row] =
              Expression.evaluate(expression)
            val dfResultAsExpression: Either[String, Expression[Dataset[Row]]] =
              Expression.validateExpression(Num(dfExpressionEvaluated))
            val dfResultJson: String = write(dfResultAsExpression)
            val result = dfExpressionEvaluated.dfToString
            ResultsHolder.sharedfExpressionEvaluated =
              Some(dfResultJson + result)
            ujson.Obj("result" -> dfResultJson)
          case Left(error) =>
            ujson.Obj("error" -> s"Invalid input: $error")
      }
    val result: Try[ujson.Obj] = eval2(argast)
    result match
      case Success(value) =>
        ujson.Obj("result" -> value.toString)
      case Failure(exception) =>
        ujson.Obj("error" -> exception.getMessage)

  initialize()

end JsonPost