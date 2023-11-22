file://<WORKSPACE>/src/main/scala/app/App.scala
### dotty.tools.dotc.ast.Trees$UnAssignedTypeException: type of Apply(Select(New(Select(Ident(cask),post)),<init>),List(Literal(Constant(/do-thing)))) is not assigned

occurred in the presentation compiler.

action parameters:
offset: 460
uri: file://<WORKSPACE>/src/main/scala/app/App.scala
text:
```scala
package app

import startup.runComputation

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
    "Hello World!" + runComputation()

  @cask.p@@ost("/do-thing")
  def doThing(request: cask.Request) =
    request.text().reverse + runComputation2()

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
  val allRoutes = Seq(MinimalRoutes(), FormJsonPost())
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