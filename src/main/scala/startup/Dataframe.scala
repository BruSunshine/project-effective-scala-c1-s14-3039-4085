package startup.dataframe

import scala.concurrent.{Future, ExecutionContext}
import ExecutionContext.Implicits.global

import startup.ast.{Expression, Mult, Plus, Num}
import org.apache.spark.sql.{SparkSession, Dataset, Row}
import org.apache.spark.sql.types._

object SparkJob:

  def run(): Future[Unit] = Future {
  
  val spark = SparkSession
    .builder()
    .appName("Spark dataframes for processing in startup.ast")
    .master("local[*]")
    .config("spark.executor.memory", "1g")
    .config("spark.executor.cores", "1")
    .config("spark.log.level", "WARN")
    .getOrCreate()

  val schema: StructType = StructType(
    Array(
      StructField("index", IntegerType, nullable = false),
      StructField("doubleField", DoubleType, nullable = false),
      StructField("stringField", StringType, nullable = false),
      StructField("intField", IntegerType, nullable = false)
    )
  )

  val data = Seq(
    Row(1, 1.0, "a", 1),
    Row(2, 2.0, "b", 2),
    Row(3, 3.0, "c", 3),
    Row(4, 4.0, "d", 4),
    Row(5, 5.0, "e", 5)
  )

  val rddDummy = spark.sparkContext.parallelize(data, 1)
  val dfDummy = spark.createDataFrame(rddDummy, schema)

  val dfExpression: Expression[Dataset[Row]] =
    Mult(Plus(Num(dfDummy), Num(dfDummy)), Num(dfDummy)) //OK
    //Mult(Plus(Num(dfDummy), Num(dfDummy)), Plus(Num(dfDummy), Num(dfDummy))) //OK
    //Plus(Num(dfDummy), Num(dfDummy)) //OK
    //Mult(Num(dfDummy), Num(dfDummy)) //OK
    //Num(dfDummy) //OK

  extension(df: Dataset[Row])
    def dfShowString: String =
      val dfRows = df.collect()
      val dfString = dfRows.map(row => row.toSeq.mkString(", ")).mkString("\n")
      dfString
  
  val dfString: String = dfDummy.dfShowString
}