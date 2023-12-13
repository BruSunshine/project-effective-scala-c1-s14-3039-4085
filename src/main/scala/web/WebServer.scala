package web

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

object ResultsHolder:
  var shareintExpressionEvaluated: Either[List[String], Option[Int]] = Right(
    None
  )
  var sharedfExpressionEvaluated: Either[List[String], Option[String]] = Right(
    None
  )

object MinimalRoutes extends cask.Routes:

  @cask.get("/resultsstring1")
  def getResultNumbers() =
    s"""|This is the result the ast evaluation 
        |on numbers ${ResultsHolder.shareintExpressionEvaluated.right.get}""".stripMargin

  @cask.get("/resultsstring2")
  def getResultDataframe() =
    s"""|This is the result the ast evaluation 
        |on dataframes ${ResultsHolder.sharedfExpressionEvaluated.right.get}""".stripMargin

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
    def tryeval1(argast: ujson.Value): Try[ujson.Obj] =
      val ret = 9
      Try {
        val eitherExpression: Either[List[String], Expression[Int]] =
          read[Either[List[String], Expression[Int]]](argast)
        eitherExpression match
          case Right(expression) =>
            val intResultAsExpression = Expression
              .evaluateValidExpression(
                Expression.validateExpression(expression)
              )
              .flatMap(x => Expression.validateExpression(Num(x)))
            val intResultJson: String = write(intResultAsExpression)
            intResultAsExpression match
              case Right(_) => 
                val result = Expression
                  .evaluateValidExpression(intResultAsExpression)
                  .right
                  .get
                ResultsHolder.shareintExpressionEvaluated =
                  Right(Some(result))
              case Left(_) => 
                ResultsHolder.shareintExpressionEvaluated =
                  Right(None)
            ujson.Obj("result" -> intResultJson)
            val stpa = 0
            //val intExpressionEvaluated = Expression.evaluateValidExpression(
            //  Expression.validateExpression(expression)
            //)
            //ResultsHolder.shareintExpressionEvaluated =
            //  intExpressionEvaluated.map(x => Some(x))
              // Some(intExpressionEvaluated)
            ujson.Obj("result" -> intResultJson)//intExpressionEvaluated.right.get)
          case Left(error) =>
            val dfErrorJson: String = write(eitherExpression)
            ujson.Obj("error" -> dfErrorJson)//s"Invalid input: $error")
      }
    end tryeval1
    val result: Try[ujson.Obj] = tryeval1(argast)
    result match
      case Success(value) => value
        //ujson.Obj("result" -> value.toString)
      case Failure(exception) =>
        ujson.Obj("error" -> exception.getMessage)
  /*
  def evaluate1(argast: ujson.Value): ujson.Obj =
    def eval1(argast: ujson.Value): Try[ujson.Obj] =
      Try {
        val eitherExpression: Either[List[String], Expression[Int]] =
          read[Either[List[String], Expression[Int]]](argast)
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
   */

  @cask.postJson("/evaluate2")
  def evaluate2(argast: ujson.Value): ujson.Obj =
    def tryeval2(argast: ujson.Value): Try[ujson.Obj] =
      Try {
        val eitherExpression: Either[List[String], Expression[Dataset[Row]]] =
          read[Either[List[String], Expression[Dataset[Row]]]](argast)
        eitherExpression match
          case Right(expression) =>
            val dfResultAsExpression = Expression
              .evaluateValidExpression(
                Expression.validateExpression(expression)
              )
              .flatMap(x => Expression.validateExpression(Num(x)))
            val dfResultJson: String = write(dfResultAsExpression)
            
            dfResultAsExpression match
              case Right(_) => 
                val result = Expression
                  .evaluateValidExpression(dfResultAsExpression)
                  .right
                  .get
                  .dfToString
                ResultsHolder.sharedfExpressionEvaluated =
                  Right(Some(dfResultJson + result))
              case Left(_) => 
                ResultsHolder.sharedfExpressionEvaluated =
                  Right(None)
            
            //val result = Expression
            //  .evaluateValidExpression(dfResultAsExpression)
            //  .right
            //  .get
            //  .dfToString
            //ResultsHolder.sharedfExpressionEvaluated =
            //  Right(Some(dfResultJson + result))
            
            ujson.Obj("result" -> dfResultJson)
          case Left(error) =>
            val dfErrorJson: String = write(eitherExpression)
            ujson.Obj("error" -> dfErrorJson)//s"Invalid input: $error")//error)//s"Invalid input: $error")
      }
    end tryeval2
    val result: Try[ujson.Obj] = tryeval2(argast)
    result match
      case Success(value) => value
      case Failure(exception) =>
        ujson.Obj("error" -> exception.getMessage)
  /*
  def evaluate2(argast: ujson.Value): ujson.Obj =
    def eval2(argast: ujson.Value): Try[ujson.Obj] =
      Try {
        val eitherExpression: Either[List[String], Expression[Dataset[Row]]] =
          read[Either[List[String], Expression[Dataset[Row]]]](argast)
        eitherExpression match
          case Right(expression) =>
            val dfExpressionEvaluated: Dataset[Row] =
              Expression.evaluate(expression)
            val dfResultAsExpression: Either[List[String], Expression[Dataset[Row]]] =
              Expression.validateExpression1(Num(dfExpressionEvaluated))
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
   */
  initialize()

end JsonPost
