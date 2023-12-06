file://<WORKSPACE>/src/main/scala/web/WebServer.scala
### dotty.tools.dotc.ast.Trees$UnAssignedTypeException: type of Apply(Select(New(Select(Ident(cask),postForm)),<init>),List(Literal(Constant(/evaluate)))) is not assigned

occurred in the presentation compiler.

action parameters:
offset: 2260
uri: file://<WORKSPACE>/src/main/scala/web/WebServer.scala
text:
```scala
package web

import startup.computation.{runComputation2}
import startup.ast.{myStart, Expression, Num}
import startup.ast.DataFrameName.given
import sparkjobs.SparkJob
import org.apache.spark.sql.{Dataset, Row}
import org.apache.spark.sql.functions._
import cask.model.Request
import io.undertow.util.StatusCodes
import upickle.default.{read, write}
import concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Await}
import scala.concurrent.duration._
import app.SparkMain.sparkSession

object MinimalRoutes extends cask.Routes:
  
  // Testing get routes
  
  @cask.get("/")
  def hello() =
   "Hello, this is the result of the computation: " + runComputation2()
  
  @cask.get("/comp")
  def comp(): cask.Response[String] =
    val futureResponse: Future[cask.Response[String]] =
      Future {
      val result = 
        Thread.sleep(5000)
        "I just slept 5 seconds. I was a future response with await time 6 seconds. This is why you see me."
      cask.Response(result.toString)
    }
    val response: cask.Response[String] = Await.result(futureResponse, 6.seconds)
    response

  @cask.get("/comp1")
  def showDf(): cask.Response[String] =
    val sparkJobResult: Future[cask.Response[String]] =
      Future {
          val dfstring = SparkJob.convertDummyDfAsString1(sparkSession)
            dfstring match
              case Right(result) =>
                cask.Response(data=result, statusCode = StatusCodes.ACCEPTED)
              case Left(errorMessage) =>
                cask.Response(data=s"Error: $errorMessage", statusCode = StatusCodes.INTERNAL_SERVER_ERROR)
      }
    val finalResponse: cask.Response[String] = Await.result(sparkJobResult, 10.seconds)
    finalResponse
  
  // Testing post routes

  @cask.post("/do-thing")
  def doThing(request: cask.Request) =
    request.text().reverse

  @cask.post("/do-parse")
  def doParse(request: cask.Request) =
    request.text().reverse

  initialize()

end MinimalRoutes


// Testing static file data acquisition route through a form
object StaticFiles extends cask.Routes:

  @cask.staticFiles("/static")
  def staticFiles(): String =
    "src/main/resources/static"

  initialize()

end StaticFiles

object FormPost extends cask.Routes:

  @cask.postFor@@m("/evaluate")
  def evaluate(
      value1: cask.FormValue,
      value2: cask.FormValue,
      value3: cask.FormValue
  ): String =
    val operation: String = value1.value
    val a: Int = value2.value.toInt
    val b: Int = value3.value.toInt
    s"ok $operation with $a and $b"

  initialize()

object JsonPost extends cask.Routes:

  @cask.postJson("/json")
  def jsonEndpoint(arg: ujson.Value): String =
    val myStartRead: myStart = read[myStart](arg)
    val result = "OK " + myStartRead.add(2, 3).toString()
    result

  @cask.postJson("/jsonast")
  def jsonEndpointAst(argast: ujson.Value): Int =
    val intExpressionRead: Expression[Int] = read[Expression[Int]](argast)
    val intExpressionEvaluated: Int = Expression.evaluate(intExpressionRead)
    intExpressionEvaluated
  
  @cask.postJson("/jsonastvalid")
  def jsonEndpointAstValid(argast: ujson.Value): Int =
    val eitherExpression: Either[String, Expression[Int]] = read[Either[String, Expression[Int]]](argast)
    eitherExpression match
    case Right(expression) => 
      val intExpressionEvaluated: Int = Expression.evaluate(expression)
      intExpressionEvaluated
    case Left(error) => 
      throw new Exception(s"Invalid input: $error")

  @cask.postJson("/jsonastdf")
  def jsonEndpointAstDf(argast: ujson.Value): String =
    val dfExpressionRead: Expression[Dataset[Row]] =
      read[Expression[Dataset[Row]]](argast)
    val dfExpressionEvaluated: Dataset[Row] =
      Expression.evaluate(dfExpressionRead)
    val dfResultAsExpression: Expression[Dataset[Row]] = Num(
      dfExpressionEvaluated
    )
    val dfResultJson: String = write(dfResultAsExpression)
    dfResultJson
  
  @cask.postJson("/jsonastdfvalid")
  def jsonEndpointAstDfValid(argast: ujson.Value): String =
    val eitherExpression: Either[String, Expression[Dataset[Row]]] =
      read[Either[String, Expression[Dataset[Row]]]](argast)
    eitherExpression match
      case Right(expression) =>
        val dfExpressionEvaluated: Dataset[Row] = Expression.evaluate(expression)
        val dfResultAsExpression: Either[String, Expression[Dataset[Row]]] =
          Expression.validateExpression(Num(dfExpressionEvaluated))
        val dfResultJson: String = write(dfResultAsExpression)
        dfResultJson
      case Left(error) => throw new Exception(s"Invalid input: $error")

  initialize()

```



#### Error stacktrace:

```
dotty.tools.dotc.ast.Trees$Tree.tpe(Trees.scala:72)
	scala.meta.internal.mtags.MtagsEnrichments$.tryTail$1(MtagsEnrichments.scala:262)
	scala.meta.internal.mtags.MtagsEnrichments$.expandRangeToEnclosingApply(MtagsEnrichments.scala:279)
	scala.meta.internal.pc.HoverProvider$.hover(HoverProvider.scala:48)
	scala.meta.internal.pc.ScalaPresentationCompiler.hover$$anonfun$1(ScalaPresentationCompiler.scala:329)
```
#### Short summary: 

dotty.tools.dotc.ast.Trees$UnAssignedTypeException: type of Apply(Select(New(Select(Ident(cask),postForm)),<init>),List(Literal(Constant(/evaluate)))) is not assigned