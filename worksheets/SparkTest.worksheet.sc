import startup.Num
import cask.model.Response.Data
//import $ivy.`org.apache.spark::spark-core:3.5.0`
//import $ivy.`org.apache.spark::spark-sql:3.5.0`

import org.apache.spark.sql.{SparkSession, DataFrame, Dataset, Row}
import org.apache.spark.sql._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._

val x: Int = 3

val spark = SparkSession.builder()
      .appName("Spark Parquet Example")
      .master("local[*]")
      .config("spark.executor.memory", "1g")
      .config("spark.executor.cores", "2")
      .getOrCreate()

import spark.implicits._

//case class MyRow(doubleField: Double, stringField: String, intField: Int)

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

//val df = spark.createDataFrame(data)
//val df = data.toDF("doubleField", "stringField", "intField")

df.show()

df.hashCode

df.write.mode("overwrite").parquet("./dataframes/file.parquet")

val dfRead: DataFrame = spark.read.parquet("./dataframes/file.parquet")

dfRead.show()

val doubledDf = df.select(
  (col("doubleField") + col("doubleField")).as("doubleField"),
  concat(col("stringField"), col("stringField")).as("stringField"),
  (col("intField") + col("intField")).as("intField")
)

doubledDf.show()

//spark.stop()

import startup.{Expression, Num, Plus, Mult}

val myexpr: Expression[Dataset[Row]] = Mult(Plus(Num(df), Num(df)), Num(df))
//val myexpr: Expression[Dataset[Row]] = Plus(Num(df), Num(df))

val result = Expression.evaluate(myexpr)

result.show()

result.select(col("doubleField"))

//import startup.given_Writer_Dataset
//import startup.ArgAst.given_RW_ArgAst

//val myAstInstance: Expression[Dataset[Row]] = myexpr
//val argAstInstance = startup.ArgAst(myAstInstance)
//val jsonArgAstString = upickle.default.write(argAstInstance)
