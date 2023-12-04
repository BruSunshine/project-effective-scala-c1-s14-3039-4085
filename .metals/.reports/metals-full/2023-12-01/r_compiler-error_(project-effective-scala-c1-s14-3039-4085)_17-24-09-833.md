file://<WORKSPACE>/src/main/scala/web/WebServer.scala
### dotty.tools.dotc.ast.Trees$UnAssignedTypeException: type of Apply(Select(New(Select(Ident(cask),post)),<init>),List(Literal(Constant(/do-thing)))) is not assigned

occurred in the presentation compiler.

action parameters:
offset: 1703
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
import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.{Failure, Success}

object MinimalRoutes extends cask.Routes:

  @cask.get("/")
  def hello() =
   "Hello World!" + runComputation2()
  
  @cask.get("/comp")
  def comp(): Future[cask.Response[String]] =
    Future {
      val result = 
        Thread.sleep(5000)
        50
      cask.Response(result.toString)
    }
  
  /*
  @cask.get("/")
  def showDf() =
    val response = cask.Response("", statusCode = StatusCodes.ACCEPTED)
    val result =
      SparkJob
        .runConvertDummyDfAsString()
        .flatMap(x =>
          x match
            case Right(result) =>
              Future.successful(
                response.copy(data = result, statusCode = StatusCodes.OK)
              )
            case Left(errorMessage) =>
              Future.successful(
                response.copy(
                  data = s"Error: $errorMessage",
                  statusCode = StatusCodes.INTERNAL_SERVER_ERROR
                )
              )
        )
        .recover({ case ex: Exception =>
          cask.Response(
            s"Unexpected error: ${ex.getMessage}",
            statusCode = StatusCodes.INTERNAL_SERVER_ERROR
          )
        })
    result
  */

  @cask.po@@st("/do-thing")
  def doThing(request: cask.Request) =
    request.text().reverse

  @cask.post("/do-parse")
  def doParse(request: cask.Request) =
    request.text().reverse

  initialize()

object StaticFiles extends cask.Routes:

  @cask.staticFiles("/static")
  def staticFiles(): String =
    "src/main/resources/static"

  initialize()

object FormPost extends cask.Routes:

  @cask.postForm("/evaluate")
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

  initialize()

//object MinimalRoutesMain extends cask.Main:
//  val allRoutes = Seq(MinimalRoutes, StaticFiles, FormPost, JsonPost)

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

dotty.tools.dotc.ast.Trees$UnAssignedTypeException: type of Apply(Select(New(Select(Ident(cask),post)),<init>),List(Literal(Constant(/do-thing)))) is not assigned