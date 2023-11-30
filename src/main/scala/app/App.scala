package app

import startup.computation.{runComputation2}
import startup.ast.{myStart, Expression, Num}
import startup.ast.DataFrameName.given
import startup.dataframe.dfString
import startup.dataframe.dfDummy
import org.apache.spark.sql.{Dataset, Row}
import org.apache.spark.sql.functions._
import cask.model.Request
import upickle.default.{read, write}

object MinimalRoutes extends cask.Routes:

  @cask.get("/")
  def hello() =
    //val dfString: String = dfDummy.dfShowString
    //"Hello World!" + runComputation2()
    dfString

  @cask.post("/do-thing")
  def doThing(request: cask.Request) =
    request.text().reverse

  @cask.post("/do-parse")
  def doParse(request: cask.Request) =
    request.text().reverse

  initialize()

object StaticFiles extends cask.Routes:

  @cask.staticFiles("/static")
  def staticFiles(): String =
    "src/main/scala/resources/static"
  
  initialize()

object FormPost extends cask.Routes:

  @cask.postForm("/evaluate")
  def evaluate(value1: cask.FormValue, value2: cask.FormValue, value3: cask.FormValue): String =
    val operation: String = value1.value
    val a: Int = value2.value.toInt
    val b: Int = value3.value.toInt
    s"ok $operation with $a and $b"
  
  initialize()


object JsonPost extends cask.Routes:

  @cask.postJson("/json")
  def jsonEndpoint(arg: ujson.Value): String =
    val myStartRead: myStart = read[myStart](arg)
    val result = "OK " + myStartRead.add(2,3).toString()
    result

  @cask.postJson("/jsonast")
  def jsonEndpointAst(argast: ujson.Value): Int =
    val intExpressionRead: Expression[Int] = read[Expression[Int]](argast)
    val intExpressionEvaluated: Int = Expression.evaluate(intExpressionRead)
    intExpressionEvaluated

  @cask.postJson("/jsonastdf")
  def jsonEndpointAstDf(argast: ujson.Value): String =
    val dfExpressionRead: Expression[Dataset[Row]] = read[Expression[Dataset[Row]]](argast)
    val dfExpressionEvaluated: Dataset[Row] = Expression.evaluate(dfExpressionRead)
    val dfResultAsExpression: Expression[Dataset[Row]] = Num(dfExpressionEvaluated)
    val dfResultJson: String = write(dfResultAsExpression)
    dfResultJson

  initialize()

/*
object MinimalRoutesMain extends cask.Main:
  val allRoutes = Seq(MinimalRoutes, StaticFiles, FormPost, JsonPost)
*/

object MinimalRoutesMain extends cask.Main:
  val allRoutes = Seq(MinimalRoutes, StaticFiles, FormPost, JsonPost)