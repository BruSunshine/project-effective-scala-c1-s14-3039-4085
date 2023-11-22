id: file://<WORKSPACE>/src/main/scala/app/App.scala:[1543..1544) in Input.VirtualFile("file://<WORKSPACE>/src/main/scala/app/App.scala", "package app

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

import upickle.default.{read}//, readwriter, macroR, macroRW, Reader, ReadWriter}
//import startup.myStart
case class FormJsonPost()(implicit cc: castor.Context, log: cask.Logger) extends cask.Routes:
object 
  //implicit val myClassReader: Reader[startup.myStart] = macroR
  //implicit val myClassReadWriter: ReadWriter[startup.myStart] = macroRW
  @cask.postJson("/json")
  def jsonEndpoint(param: ujson.Value) =
    //println(param.value)
    //implicit val myClassReadWriter: ReadWriter[myStart] = startup.myStart.rw
    val myStartInstance = read[startup.myStart](param)
    "OK " + myStartInstance.add(2,3).toString()

  //@cask.postJson("/json-obj")
  //def jsonEndpointObj(value1: ujson.Value, value2: Seq[Int]) =
  //  ujson.Obj(
  //    "value1" -> value1,
  //    "value2" -> value2
  //  )
  initialize()
/*

object FormJsonPost extends cask.MainRoutes:

  @cask.postJson("/json")
  def jsonEndpoint(value1: ujson.Value, value2: Seq[Int]) =
    "OK " + value1 + " " + value2

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
*/
object MinimalRoutesMain extends cask.Main:
  val allRoutes = Seq(MinimalRoutes(), StaticFiles, FormPost, FormJsonPost())")
file://<WORKSPACE>/src/main/scala/app/App.scala
file://<WORKSPACE>/src/main/scala/app/App.scala:62: error: expected identifier; obtained at
  @cask.postJson("/json")
  ^
#### Short summary: 

expected identifier; obtained at