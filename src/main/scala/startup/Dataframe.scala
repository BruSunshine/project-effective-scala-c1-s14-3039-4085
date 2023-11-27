package startup

import org.apache.spark.sql.{SparkSession, DataFrame, Dataset, Row}
import org.apache.spark.sql._
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
//import startup.{Expression, Num, Plus, Mult}

import upickle.default.{ReadWriter => RW, macroRW, reader, writer, Reader, Writer}
import org.apache.spark.sql.{DataFrame, Dataset, Row}

val spark = SparkSession.builder()
      .appName("Spark Parquet Example")
      .master("local[*]")
      .config("spark.executor.memory", "1g")
      .config("spark.executor.cores", "2")
      .getOrCreate()

import spark.implicits._

val schema: StructType = StructType(Array(
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

val myexpr: Expression[Dataset[Row]] = Mult(Plus(Num(df), Num(df)), Num(df))
//val myexpr: Expression[Dataset[Row]] = Plus(Num(df), Num(df))

/*
given DataFrameNameRW: RW[DataFrame] with
  def read: Reader[DataFrame] = reader[String].map(name => spark.read.parquet(name))
  def write: Writer[DataFrame] = writer[String].comap(df => {
    val path = s"./dataframes/${df.hashCode}.parquet"
    df.write.mode("overwrite").parquet(path)
    path
  })
*/  


case class DataFrameName(name: String)

given RW[DataFrameName] = macroRW

extension (df: DataFrame)
  def writeAsParquet: DataFrameName =
    val path = s"./dataframes/${df.hashCode}.parquet"
    df.write.mode("overwrite").parquet(path)
    DataFrameName(path)

extension (name: DataFrameName)
  def readAsDataFrame: DataFrame = spark.read.parquet(name.name)

given Reader[DataFrame] = reader[DataFrameName].map(_.readAsDataFrame)
given Writer[DataFrame] = writer[DataFrameName].comap(_.writeAsParquet)

/*
case class DataFrameName(name: String)

given RW[DataFrameName] = macroRW

def dataframeToName(df: Dataset[Row]): DataFrameName =
  val path = s"./dataframes/${df.hashCode}.parquet"
  df.write.mode("overwrite").parquet(path)
  DataFrameName(path)

def nameToDataframe(name: DataFrameName): DataFrame =
  spark.read.parquet(name.name)

given Reader[Dataset[Row]] = reader[DataFrameName].map(nameToDataframe)
given Writer[Dataset[Row]] = writer[DataFrameName].comap(dataframeToName)
*/

//given DataFrameNameRW: RW[DataFrame] with
//  def read: Reader[DataFrame] = reader[DataFrameName].map(nameToDataframe)
//  def write: Writer[DataFrame] = writer[DataFrameName].comap(dataframeToName)

//import startup.ArgAst.given_RW_ArgAst

val myAstInstance: Expression[Dataset[Row]] = myexpr
val argAstInstance = ArgAst(myAstInstance)
val jsonArgAstString = upickle.default.write(argAstInstance)

