import $ivy.`com.softwaremill.sttp.client3::core:3.9.1`
import $ivy.`com.lihaoyi::upickle:3.1.3`

import startup.ast.{Expression, ArithmeticOperation, Num, Mult, Plus, myStart, Arg, ExpressionToSerialize}
import upickle.default.{write, read}

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

//val x: Int = 1
//val y: Int = 5
//val z: Int = 7
//val expr: Expression[Int] = Mult(Plus(Num(x), Num(y)), Num(z))
//implicit val writer: Writer[startup.Expression[Int]] = macroW
//val jsonString = write(expr)


val myint:Int = 123
val myDataInstance = myStart(myint)
//myDataInstance.add(1,2).toString()
//implicit val writer: Writer[startup.myStart] = startup.myStart.rw//
val jsonString = write(myDataInstance)
print(jsonString)
//val jsonObj = ujson.Obj("arg" -> jsonString)
//print(jsonString)
//val jsonValue: ujson.Value = writeJs(myDataInstance)
val argInstance = Arg(myDataInstance)
val jsonArgString = write(argInstance)

val response3 = basicRequest
  .post(uri"http://localhost:8080/json")
  .body(jsonArgString)//.body(jsonValue.toString) jsonString
  .send(backend)

println(response3.body)


val myStartInstance = read[myStart](jsonString)
myStartInstance.add(1,2)

//jsonString.intern()
//ujson.Obj("df" -> s"${jsonString.intern()}")

val json: ujson.Value = ujson.read(jsonString)

json.toString

("5").toString()


// Testing the serialization and deserialization of the AST with Integers


val myAstInstance: Expression[Int] = Plus(Num(3), Num(4))
Expression.evaluate(myAstInstance)

val jsonStringAst = write[Expression[Int]](myAstInstance)
val myAstInstanceRead = read[Expression[Int]](jsonStringAst)
Expression.evaluate(myAstInstanceRead)

val argAstInstance = ExpressionToSerialize[Int](myAstInstance)
val jsonArgAstString = write[ExpressionToSerialize[Int]](argAstInstance)

val response4 = basicRequest
  .post(uri"http://localhost:8080/jsonast")
  .body(jsonArgAstString)//.body(jsonValue.toString) jsonString
  .send(backend)

println(response4.body)


// Testing the serialization and deserialization of the AST with dataframes
/*
import org.apache.spark.sql.{SparkSession, DataFrame, Dataset, Row}
import org.apache.spark.sql._
import org.apache.spark.sql.types._


val spark = SparkSession.builder()
      .appName("Spark Parquet Example")
      .master("local[*]")
      .config("spark.executor.memory", "1g")
      .config("spark.executor.cores", "2")
      .getOrCreate()


val schema = StructType(Array(
  StructField("doubleField", DoubleType, nullable = false),
  StructField("stringField", StringType, nullable = false),
  StructField("intField", IntegerType, nullable = false)
))

val data = Seq(
  Row(1.0, "a", 1),
  Row(2.0, "b", 2),
  Row(3.0, "c", 3),
  Row(4.0, "d", 4),
  Row(5.0, "e", 5)
)

val rdd = spark.sparkContext.parallelize(data)
val df = spark.createDataFrame(rdd, schema)

df.show()

val myexpr: Expression[Dataset[Row]] = Mult(Plus(Num(df), Num(df)), Num(df))
val result = Expression.evaluate(myexpr)

result.show()
import startup.ast.DataFrameName.given
val myAstInstance1: Expression[Dataset[Row]] = myexpr
val argAstInstance1 = ExpressionToSerialize(myAstInstance1)
val jsonArgAstString1 = write(argAstInstance1)

val response5 = basicRequest
  .post(uri"http://localhost:8080/jsonastdf")
  .body(jsonArgAstString1)//.body(jsonValue.toString) jsonString
  .send(backend)

println(response5.body)
*/

import sparkjobs.SparkJob.{makeDummyDf, MakeExpression, runMakeDummyDf, runConvertDummyDfAsString}

val df = makeDummyDf()
df.show()

MakeExpression(df)

runMakeDummyDf()

runConvertDummyDfAsString()