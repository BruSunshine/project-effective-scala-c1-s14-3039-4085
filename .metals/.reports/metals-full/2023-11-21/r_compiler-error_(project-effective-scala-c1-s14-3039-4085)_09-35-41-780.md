file://<WORKSPACE>/src/main/scala/app/App.scala
### dotty.tools.dotc.ast.Trees$UnAssignedTypeException: type of Apply(Select(New(Select(Ident(cask),postForm)),<init>),List(Ident(/))) is not assigned

occurred in the presentation compiler.

action parameters:
offset: 491
uri: file://<WORKSPACE>/src/main/scala/app/App.scala
text:
```scala
package app

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
  
  @cask.postForm(/@@)

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
  val allRoutes = Seq(MinimalRoutes(), FormJsonPost())
```



#### Error stacktrace:

```
dotty.tools.dotc.ast.Trees$Tree.tpe(Trees.scala:72)
	dotty.tools.dotc.util.Signatures$.isValid(Signatures.scala:305)
	dotty.tools.dotc.util.Signatures$.findEnclosingApply$$anonfun$1(Signatures.scala:112)
	scala.collection.immutable.List.filterNot(List.scala:515)
	dotty.tools.dotc.util.Signatures$.findEnclosingApply(Signatures.scala:117)
	dotty.tools.dotc.util.Signatures$.computeSignatureHelp(Signatures.scala:93)
	dotty.tools.dotc.util.Signatures$.signatureHelp(Signatures.scala:63)
	scala.meta.internal.pc.MetalsSignatures$.signatures(MetalsSignatures.scala:17)
	scala.meta.internal.pc.SignatureHelpProvider$.signatureHelp(SignatureHelpProvider.scala:51)
	scala.meta.internal.pc.ScalaPresentationCompiler.signatureHelp$$anonfun$1(ScalaPresentationCompiler.scala:375)
```
#### Short summary: 

dotty.tools.dotc.ast.Trees$UnAssignedTypeException: type of Apply(Select(New(Select(Ident(cask),postForm)),<init>),List(Ident(/))) is not assigned