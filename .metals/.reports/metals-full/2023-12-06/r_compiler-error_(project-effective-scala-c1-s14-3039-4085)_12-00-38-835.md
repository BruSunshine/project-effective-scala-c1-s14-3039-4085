file://<WORKSPACE>/src/main/scala/web/WebServer.scala
### dotty.tools.dotc.ast.Trees$UnAssignedTypeException: type of Apply(Select(New(Select(Ident(cask),postJson)),<init>),List(Literal(Constant(/evaluate1)))) is not assigned

occurred in the presentation compiler.

action parameters:
offset: 6820
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
import scala.io.Source

object ResultsHolder:
  //var lastResult: String = ""
  var sharedx: Option[Int] = None
  var sharedy: Option[Int] = None
  var sharedz: Option[Int] = None
  var shareJsonString: Option[String] = None
  var shareintExpressionEvaluated: Option[Int] = None

object MinimalRoutes extends cask.Routes:
  
  //@cask.get("/")
  //def root() =
  //  //java.nio.file.Paths.get("src/main/resources/static/toc.html")
  //  val tocHtml = Source.fromResource("static/toc.html").mkString
  //  cask.Response(tocHtml, headers = Seq("Content-Type" -> "text/html"))

  // Testing get routes
  
  @cask.get("/results")
  def hello() =
   "Hello, this is the result of the computation: " + runComputation2(ResultsHolder.sharedx, ResultsHolder.sharedy, ResultsHolder.sharedz)

  //@cask.get("/resultsstring")
  //def hellojson() =
  //  s"Hello, this is the result string ${ResultsHolder.shareJsonString}"
  @cask.get("/resultsstring")
  def hellojson() =
    s"Hello, this is the result string ${ResultsHolder.shareintExpressionEvaluated}"

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

  @cask.postForm("/evaluate")
  def evaluate(
      value1: cask.FormValue,
      value2: cask.FormValue,
      value3: cask.FormValue,
      value4: cask.FormValue
  ): String =
    val operation: String = value1.value
    val a: Int = value2.value.toInt
    val b: Int = value3.value.toInt
    val c: Int = value4.value.toInt
    
    ResultsHolder.sharedx = Some(a)
    ResultsHolder.sharedy = Some(b)
    ResultsHolder.sharedz = Some(c)
    
    s"ok $operation with $a and $b"

  //@cask.postForm("/evaluate1")
  //def evaluate1(request: cask.Request): String =//ujson.Value =
  //  val jsonStr = request.text()
  //  ResultsHolder.shareJsonString = Some(jsonStr)
  //  jsonStr
  
  //@cask.postForm("/evaluate1")
  //def evaluate1(json: cask.FormValue): String =
  //  val jsonStr = json.value
  //  ResultsHolder.shareJsonString = Some(jsonStr)
  //  println(jsonStr)
  //  jsonStr


  initialize()

end FormPost

// Testing serializing objects and posting them to server as json for evaluation
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
    println(argast)
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
  
  @cask.postJson("/evaluate1")
  def evaluate1(argast: ujson.Value): ujson.Obj =
    println(argast)
    val eitherExpression: Either[String, Expression[Int]] = read[Either[String, Expression[Int]]](argast)
    eitherExpression match
    case Right(expression) => 
      val intExpressionEvaluated: Int = Expression.evaluate(expression)
      ResultsHolder.shareintExpressionEvaluated = Some(intExpressionEvaluated)
      ujson.Obj("result" -> intExpressionEvaluated)
      //intExpressionEvaluated
    case Left(error) => 
      ujson.Obj("error" -> s"Invalid input: $error")
      //throw new Exception(s"Invalid input: $error")

  @cask.pos@@tJson("/evaluate1")
  def evaluate1(argast: ujson.Value): ujson.Obj =
    println(argast)
    val eitherExpression: Either[String, Expression[Int]] = read[Either[String, Expression[Int]]](argast)
    eitherExpression match
    case Right(expression) => 
      val intExpressionEvaluated: Int = Expression.evaluate(expression)
      ResultsHolder.shareintExpressionEvaluated = Some(intExpressionEvaluated)
      ujson.Obj("result" -> intExpressionEvaluated)
      //intExpressionEvaluated
    case Left(error) => 
      ujson.Obj("error" -> s"Invalid input: $error")
      //throw new Exception(s"Invalid input: $error")

  initialize()

end JsonPost

// Final project objective:
// Get parameters for an expression to evaluate through a form typically @cask.staticFiles("/static")
// Build and transform the corresponding expression typically included in the next step
// through the evaluate button on the form, post a request for evaluation typically @cask.postForm("/evaluate")
// write the name of the dataframe into the memory to be able to get the results
// get the results on a result page which load the corresponsing dataframe for display: @cask.get("/comp1")

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

dotty.tools.dotc.ast.Trees$UnAssignedTypeException: type of Apply(Select(New(Select(Ident(cask),postJson)),<init>),List(Literal(Constant(/evaluate1)))) is not assigned