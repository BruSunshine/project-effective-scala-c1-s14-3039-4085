file://<WORKSPACE>/worksheets/ApiTest.worksheet.sc
### java.lang.AssertionError: NoDenotation.owner

occurred in the presentation compiler.

action parameters:
offset: 2513
uri: file://<WORKSPACE>/worksheets/ApiTest.worksheet.sc
text:
```scala
object worksheet{
  import $ivy.`com.softwaremill.sttp.client3::core:3.9.1`
  import $ivy.`com.lihaoyi::upickle:3.1.3`
  
  import sttp.client3._
  
  val backend = HttpURLConnectionBackend()
  
  /*
  val response0 = basicRequest
    .get(uri"http://localhost:8080/evaluate")
    .send(backend)
  
  println(response0.body)
  */
  
  // Test 0
  
  val response0 = basicRequest
    .post(uri"http://localhost:8080/do-thing")
    .body("hello")
    .send(backend)
  
  println(response0.body)
  
  // Test 1
  
  val response1 = basicRequest
    .post(uri"http://localhost:8080/evaluate")
    .body("value1=mult&value2=1&value3=4")
    .send(backend)
  
  println(response1.body)
  
  
  // Test 2
  
  val response2 = basicRequest
    .post(uri"http://localhost:8080/do-parse")
    .body("value1=mult&value2=1&value3=4")
    .send(backend)
  
  println(response2.body)
  
  
  
  // Test 3
  
  //import startup._//{myStart, Expression}
  import upickle.default.{write}
  //import startup.myStart
  
  //val x: Int = 1
  //val y: Int = 5
  //val z: Int = 7
  //val expr: Expression[Int] = Mult(Plus(Num(x), Num(y)), Num(z))
  //implicit val writer: Writer[startup.Expression[Int]] = macroW
  //val jsonString = write(expr)
  
  
  val myint:Int = 123
  val myDataInstance = startup.myStart(myint)
  //myDataInstance.add(1,2).toString()
  //implicit val writer: Writer[startup.myStart] = startup.myStart.rw//
  val jsonString = write(myDataInstance)
  print(jsonString)
  //val jsonObj = ujson.Obj("arg" -> jsonString)
  //print(jsonString)
  //val jsonValue: ujson.Value = writeJs(myDataInstance)
  val argInstance = startup.Arg(myDataInstance)
  val jsonArgString = write(argInstance)
  
  val response3 = basicRequest
    .post(uri"http://localhost:8080/json")
    .body(jsonArgString)//.body(jsonValue.toString) jsonString
    .send(backend)
  
  println(response3.body)
  
  import upickle.default.{read}
  val myStartInstance = read[startup.myStart](jsonString)
  myStartInstance.add(1,2)
  
  //jsonString.intern()
  //ujson.Obj("df" -> s"${jsonString.intern()}")
  
  val json: ujson.Value = ujson.read(jsonString)
  
  json.toString
  
  ("5").toString()
  
  
  // Testing the serialization and deserialization of the AST
  
  import startup.{Expression,ArithmeticOperation ,Mult, Num, Plus}
  
  val myAstInstance: Expression[Int] = Plus(Num(3), Num(4))
  Expression.evaluate(myAstInstance)
  
  val jsonStringAst = write(myAstInstance)
  print(jsonStringAst)
  
  val argAstInstance = startup.ArgAst[]@@(myAstInstance)
  val jsonArgString = write(argAstInstance)
}
```



#### Error stacktrace:

```
dotty.tools.dotc.core.SymDenotations$NoDenotation$.owner(SymDenotations.scala:2582)
	scala.meta.internal.pc.SignatureHelpProvider$.isValid(SignatureHelpProvider.scala:83)
	scala.meta.internal.pc.SignatureHelpProvider$.notCurrentApply(SignatureHelpProvider.scala:96)
	scala.meta.internal.pc.SignatureHelpProvider$.$anonfun$1(SignatureHelpProvider.scala:48)
	scala.collection.StrictOptimizedLinearSeqOps.loop$3(LinearSeq.scala:280)
	scala.collection.StrictOptimizedLinearSeqOps.dropWhile(LinearSeq.scala:282)
	scala.collection.StrictOptimizedLinearSeqOps.dropWhile$(LinearSeq.scala:278)
	scala.collection.immutable.List.dropWhile(List.scala:79)
	scala.meta.internal.pc.SignatureHelpProvider$.signatureHelp(SignatureHelpProvider.scala:48)
	scala.meta.internal.pc.ScalaPresentationCompiler.signatureHelp$$anonfun$1(ScalaPresentationCompiler.scala:375)
```
#### Short summary: 

java.lang.AssertionError: NoDenotation.owner