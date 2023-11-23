file://<WORKSPACE>/src/main/scala/app/App.scala
### dotty.tools.dotc.ast.Trees$UnAssignedTypeException: type of Apply(Select(New(Select(Ident(cask),postForm)),<init>),List(Literal(Constant(/evaluate)))) is not assigned

occurred in the presentation compiler.

action parameters:
offset: 793
uri: file://<WORKSPACE>/src/main/scala/app/App.scala
text:
```scala
package app

import startup.{runComputation, runComputation2}
import upickle.default.read
import cask.model.Request

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

  @cask.po@@stForm("/evaluate")
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
    val result = "OK " + myStartInstance.add(2,3).toString()
    result

  initialize()

object MinimalRoutesMain extends cask.Main:
  val allRoutes = Seq(MinimalRoutes, StaticFiles, FormPost, JsonPost)
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