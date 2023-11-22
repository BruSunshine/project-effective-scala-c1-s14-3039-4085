id: file://<WORKSPACE>/src/main/scala/app/App.scala:[513..514) in Input.VirtualFile("file://<WORKSPACE>/src/main/scala/app/App.scala", "package app

import startup.{runComputation, runComputation2}

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
  
  @cask.postForm("/evaluate")
  def 

  @cask.post("/do-thing")
  def doThing(request: cask.Request) =
    request.text().reverse

  initialize()

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
file://<WORKSPACE>/src/main/scala/app/App.scala:27: error: expected identifier; obtained at
  @cask.post("/do-thing")
  ^
#### Short summary: 

expected identifier; obtained at