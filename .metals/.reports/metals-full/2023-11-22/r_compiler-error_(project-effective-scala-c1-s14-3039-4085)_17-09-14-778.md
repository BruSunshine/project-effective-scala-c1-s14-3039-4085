file://<WORKSPACE>/src/main/scala/app/App.scala
### dotty.tools.dotc.ast.Trees$UnAssignedTypeException: type of Apply(Select(New(Select(Ident(cask),postForm)),<init>),List(Literal(Constant(/evaluate)))) is not assigned

occurred in the presentation compiler.

action parameters:
offset: 913
uri: file://<WORKSPACE>/src/main/scala/app/App.scala
text:
```scala
package app

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

  @cask.postFor@@m("/evaluate")
  def evaluate(value1: cask.FormValue, value2: cask.FormValue, value3: cask.FormValue): String =
    val operation: String = value1.value
    val a: Int = value2.value.toInt
    val b: Int = value3.value.toInt
    s"ok $operation with $a and $b"
  
  initialize()

import upickle.default.{read}//, readwriter, macroR, macroRW, Reader, ReadWriter}
//import startup.myStart
//case class FormJsonPost()(implicit cc: castor.Context, log: cask.Logger) extends cask.Routes:
object FormJsonPost extends cask.Routes:
  //implicit val myClassReader: Reader[startup.myStart] = macroR
  //implicit val myClassReadWriter: ReadWriter[startup.myStart] = macroRW
  @cask.postJson("/json")
  def jsonEndpoint(arg: ujson.Value) = //startup.myStart) =ujson.Value
    println(arg)
    //val json= ujson.read(arg)//ujson.Value
    //println(json)
    //val numValue: ujson.Num = arg.num // Your ujson.Num value  startup.myStart
    //val intValue: Int = numValue.num.toInt // Convert to Int
    //val newValue: ujson.Value = ujson.Num(intValue) // Convert back to ujson.Value
    //implicit val myClassReadWriter: ReadWriter[myStart] = startup.myStart.rw
    val myStartInstance = read[startup.myStart](arg)
    val tempres = myStartInstance.add(2,3)
    val result = "OK " + myStartInstance.add(2,3).toString()
    result

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
  val allRoutes = Seq(MinimalRoutes(), StaticFiles, FormPost, FormJsonPost)
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