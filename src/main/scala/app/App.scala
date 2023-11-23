package app

import startup.{runComputation, runComputation2, myStart, Arg}
import cask.model.Request
import upickle.default.{ReadWriter => RW, read}

//object MinimalApplication extends cask.MainRoutes:

//case class MinimalRoutes()(implicit cc: castor.Context, log: cask.Logger) extends cask.Routes:
object MinimalRoutes extends cask.Routes:

  @cask.get("/")
  def hello() =
    "Hello World!" + runComputation2()

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
  def jsonEndpoint(arg: ujson.Value) =
    val myStartInstance = read[startup.myStart](arg)
    //val myStartInstance = summon[RW[Arg]].read(arg)
    val result = "OK " + myStartInstance.add(2,3).toString()
    result

  initialize()

object MinimalRoutesMain extends cask.Main:
  val allRoutes = Seq(MinimalRoutes, StaticFiles, FormPost, JsonPost)