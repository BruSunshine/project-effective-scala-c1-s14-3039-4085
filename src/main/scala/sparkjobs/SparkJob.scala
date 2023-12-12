package sparkjobs

import startup.ast.{Expression, Mult, Plus, Num}
import org.apache.spark.SparkConf
import org.apache.spark.sql.{SparkSession, Dataset, Row, DataFrame}
import org.apache.spark.sql.types._

extension (df: Dataset[Row])
  def dfToString: String =
    val dfRows = df.collect()
    val dfString = dfRows.map(row => row.toSeq.mkString(", ")).mkString("\n")
    dfString

object Session:

  val conf = new SparkConf().setAppName("sparkApp").setMaster("local[*]")

  lazy val spark: SparkSession =
    try
      SparkSession
        .builder()
        .config(conf)
        .config("spark.executor.memory", "1g")
        .config("spark.log.level", "WARN")
        .getOrCreate()
    catch
      case e: Exception =>
        val trace = e.printStackTrace()
        throw e

  // stopSparkSession() not currently in use
  def stopSparkSession(): Unit =
    spark.stop()

end Session

object SparkJob:

  def makeDummyDfNonValidated(
      session: SparkSession,
      schema: StructType,
      data: Seq[Row]
  ): DataFrame =
    val rddDummy = session.sparkContext.parallelize(data, 1)
    val dfDummy = session.createDataFrame(rddDummy, schema)
    dfDummy

  def makeDummyDfValidated(
      session: SparkSession,
      schema: StructType,
      data: Seq[Row]
  ): Either[List[String], DataFrame] =
    try
      val validatedDf: Either[List[String], DataFrame] = Right(
        makeDummyDfNonValidated(session, schema, data)
      )
      validatedDf
    catch case e: Exception => Left(List(s"Error processing data: ${e.getMessage}"))

  def convertDummyDfValidatedToString(
      session: SparkSession,
      schema: StructType,
      data: Seq[Row]
  ): Either[List[String], String] =
    val result = makeDummyDfValidated(session, schema, data).map(validatedDf =>
      validatedDf.dfToString.replace(",", "|")
    )
    result

  def makeDfExpressionNonValidatedMix(df: DataFrame): Expression[DataFrame] =
    val dfExpression: Expression[DataFrame] =
      Mult(Plus(Num(df), Num(df)), Num(df))
    dfExpression

  def runMakeExpressionNonValidatedMix(
      session: SparkSession,
      schema: StructType,
      data: Seq[Row]
  ): Expression[DataFrame] =
    val result = makeDfExpressionNonValidatedMix(
      makeDummyDfNonValidated(session, schema, data)
    )
    result

end SparkJob
