
//import $ivy.`org.apache.spark::spark-core:3.5.0`
//import $ivy.`org.apache.spark::spark-sql:3.5.0`

import org.apache.spark.sql.{SparkSession, DataFrame, Dataset, Row}
import org.apache.spark.sql._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._

import upickle.default.{write, Reader}

import startup.ast.{Expression, Num, Plus, Mult, ExpressionToSerialize, DataFrameName}


val spark = SparkSession.builder()
      .appName("Spark Parquet Example")
      .master("local[*]")
      .config("spark.executor.memory", "1g")
      .config("spark.executor.cores", "2")
      .getOrCreate()


//case class MyRow(doubleField: Double, stringField: String, intField: Int)

val schema = StructType(Array(
  StructField("index", IntegerType, nullable = false),
  StructField("doubleField", DoubleType, nullable = false),
  StructField("stringField", StringType, nullable = false),
  StructField("intField", IntegerType, nullable = false)
))

//val data = Seq(
//  Row(1.0, "a", 1),
//  Row(2.0, "b", 2),
//  Row(3.0, "c", 3),
//  Row(4.0, "d", 4),
//  Row(5.0, "e", 5)
//)
val data = Seq(
  Row(1, 1.0, "a", 1),
  Row(2, 2.0, "b", 2),
  Row(3, 3.0, "c", 3),
  Row(4, 4.0, "d", 4),
  Row(5, 5.0, "e", 5)
)

//def createRow(tuple: (Double, String, Int), index: Long): Row =
//  Row(index, tuple._1, tuple._2, tuple._3)

val rdd = spark.sparkContext.parallelize(data, 1)//.zipWithIndex().map(createRow)
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

val sumDf = doubledDf.agg(sum("intField"))
val sumValue = sumDf.collect()(0).mkString

//spark.stop()

val myexpr: Expression[Dataset[Row]] = Mult(Plus(Num(df), Num(df)), Num(df))
//val myexpr: Expression[Dataset[Row]] = Plus(Num(df), Num(df))

val result = Expression.evaluate(myexpr)

result.show()


result.isEmpty
//val emptyDf: DataFrame = spark.createDataFrame(spark.sparkContext.emptyRDD[Row], schema)
//emptyDf.isEmpty



result.select(col("doubleField"))

import startup.ast.DataFrameName.given
summon[Reader[Dataset[Row]]]

val myAstInstance: Expression[Dataset[Row]] = myexpr
val argAstInstance = ExpressionToSerialize(myAstInstance)
val jsonArgAstString = write(argAstInstance)


doubledDf.show()
val dfWithIndex = doubledDf.rdd.zipWithIndex().map({case (row, index) => (index, row)})//.toDF("index", "row")