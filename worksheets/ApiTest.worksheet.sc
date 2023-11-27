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

val jsonStringAst = write[Expression[Int]](myAstInstance)
print(jsonStringAst)

val myAstInstanceRead = read[Expression[Int]](jsonStringAst)
Expression.evaluate(myAstInstanceRead)

val argAstInstance = startup.ArgAst[Int](myAstInstance)
val jsonArgAstString = write[startup.ArgAst[Int]](argAstInstance)

val response4 = basicRequest
  .post(uri"http://localhost:8080/jsonast")
  .body(jsonArgAstString)//.body(jsonValue.toString) jsonString
  .send(backend)

println(response4.body)