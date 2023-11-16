file://<WORKSPACE>/src/main/scala/app/App.scala
### dotty.tools.dotc.ast.Trees$UnAssignedTypeException: type of Apply(Select(New(Select(Ident(cask),get)),<init>),List(Literal(Constant(/)))) is not assigned

occurred in the presentation compiler.

action parameters:
offset: 75
uri: file://<WORKSPACE>/src/main/scala/app/App.scala
text:
```scala
package app

object MinimalApplication extends cask.MainRoutes:
  
  @cask.@@get("/")
  def hello() =
    "Hello World!"

  @cask.post("/do-thing")
  def doThing(request: cask.Request) =
    request.text().reverse

  initialize()
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

dotty.tools.dotc.ast.Trees$UnAssignedTypeException: type of Apply(Select(New(Select(Ident(cask),get)),<init>),List(Literal(Constant(/)))) is not assigned