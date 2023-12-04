id: file://<WORKSPACE>/src/main/scala/sparkjobs/SparkJob.scala:[747..750) in Input.VirtualFile("file://<WORKSPACE>/src/main/scala/sparkjobs/SparkJob.scala", "package sparkjobs

import scala.concurrent.{Future, ExecutionContext}
import ExecutionContext.Implicits.global

import startup.ast.{Expression, Mult, Plus, Num}
import org.apache.spark.sql.{SparkSession, Dataset, Row}
import org.apache.spark.sql.types._

extension(df: Dataset[Row])
  def dfShowString: String =
    val dfRows = df.collect()
    val dfString = dfRows.map(row => row.toSeq.mkString(", ")).mkString("\n")
    dfString

object SparkJob:

  lazy val spark = SparkSession
    .builder()
    .appName("Spark dataframes for processing in startup.ast")
    .master("local[*]")
    .config("spark.executor.memory", "1g")
    .config("spark.executor.cores", "1")
    .config("spark.log.level", "WARN")
    .getOrCreate()

  private def 

  def run(): Future[Unit] = Future {
        
    try
      def runDummy(): String =

      
        val dfString: String = makeDummyDf().dfShowString
        dfString
      
      end runDummy

    finally
      spark.stop()
}


      def makeDummyExpression(): Expression[Dataset[Row]] =
        val dfDummy: Dataset[Row] = makeDummyDf()
        val dfExpression: Expression[Dataset[Row]] =
          Mult(Plus(Num(dfDummy), Num(dfDummy)), Num(dfDummy)) //OK
          //Mult(Plus(Num(dfDummy), Num(dfDummy)), Plus(Num(dfDummy), Num(dfDummy))) //OK
          //Plus(Num(dfDummy), Num(dfDummy)) //OK
          //Mult(Num(dfDummy), Num(dfDummy)) //OK
          //Num(dfDummy) //OK
        dfExpression
      end makeDummyExpression")
file://<WORKSPACE>/src/main/scala/sparkjobs/SparkJob.scala
file://<WORKSPACE>/src/main/scala/sparkjobs/SparkJob.scala:29: error: expected identifier; obtained def
  def run(): Future[Unit] = Future {
  ^
#### Short summary: 

expected identifier; obtained def