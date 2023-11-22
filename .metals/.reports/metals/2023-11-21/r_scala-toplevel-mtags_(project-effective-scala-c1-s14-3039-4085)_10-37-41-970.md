id: file://<WORKSPACE>/src/main/scala/app/App.scala:[973..974) in Input.VirtualFile("file://<WORKSPACE>/src/main/scala/app/App.scala", "package app

import startup.{runComputation, runComputation2}

import cask.model.Request


/*
object MinimalApplication extends cask.MainRoutes:

  @cask.get("/")
  def hello() =
    "Hello World!" + runComputation()

  @cask.post("/do-thing")
  def doThing(request: cask.Request) =
    request.text().reverse

  initialize()
 */

case class MinimalRoutes()(implicit cc: castor.Context, log: cask.Logger) extends cask.Routes:
  @cask.get("/")
  def hello() =
    "Hello World!" + runComputation2()
  
  @cask.get("/teststatic")
  def whereStatic(): String =
    (os.pwd / "src" / "main"/ "scala"/ "resources"/ "static").toString
  
  //@cask.postForm("/evaluate")
  //def evaluate(request: Request): String =
  //  request.
  //  val operation = request.form("operation")
  //  val a = request.form("a").toInt
  //  val b = request.form("b").toInt

  @cask.post("/do-thing")
  def doThing(request: cask.Request) =
    request.text().reverse

  initialize()

case class 

  @cask.staticFiles("/static")
  def staticFiles(): String =
    (os.pwd / "src" / "main"/ "scala"/ "resources"/ "static").toString
    //"resources/static/index.html"

case class FormJsonPost()(implicit cc: castor.Context, log: cask.Logger) extends cask.Routes:
//object FormJsonPost extends cask.MainRoutes:

  @cask.postJson("/json")
  def jsonEndpoint(value1: ujson.Value, value2: Seq[Int]) =
    "OK " + value1 + " " + value2

  @cask.postJson("/json-obj")
  def jsonEndpointObj(value1: ujson.Value, value2: Seq[Int]) =
    ujson.Obj(
      "value1" -> value1,
      "value2" -> value2
    )

  @cask.postForm("/form")
  def formEndpoint(value1: cask.FormValue, value2: Seq[Int]) =
    "OK " + value1 + " " + value2

  @cask.postForm("/form-obj")
  def formEndpointObj(value1: cask.FormValue, value2: Seq[Int]) =
    ujson.Obj(
      "value1" -> value1.value,
      "value2" -> value2
    )

  @cask.postForm("/upload")
  def uploadFile(image: cask.FormFile) =
    image.fileName

  initialize()

object MinimalRoutesMain extends cask.Main:
  val allRoutes = Seq(MinimalRoutes(), FormJsonPost())")
file://<WORKSPACE>/src/main/scala/app/App.scala
file://<WORKSPACE>/src/main/scala/app/App.scala:46: error: expected identifier; obtained at
  @cask.staticFiles("/static")
  ^
#### Short summary: 

expected identifier; obtained at