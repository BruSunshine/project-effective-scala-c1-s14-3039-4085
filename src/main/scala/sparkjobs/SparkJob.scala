package sparkjobs

//import scala.concurrent.{ExecutionContext}//Future,
//import ExecutionContext.Implicits.global
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
        // .appName("Spark dataframes for processing in startup.ast")
        // .master("local[2]")
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

  // generation of (future of) (validated) dataframe

  // def makeEmptyDf(session: SparkSession): DataFrame =
  //  val rddEmpty = session.sparkContext.parallelize(Seq.empty[Row], 1)
  //  val dfEmpty = session.createDataFrame(rddEmpty, DataFramesExemples.schema)
  //  dfEmpty

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
  ): Either[String, DataFrame] =
    try
      val validatedDf: Either[String, DataFrame] = Right(
        makeDummyDfNonValidated(session, schema, data)
      )
      validatedDf
    catch case e: Exception => Left(s"Error processing data: ${e.getMessage}")

  // def makeDummyDf2(session: SparkSession): Future[Either[String, DataFrame]] =
  //  Future {
  //    try
  //      val validatedDf: Either[String, DataFrame] = Right(makeDummyDf0(session))
  //      validatedDf
  //    catch
  //      case e: Exception => Left(s"Error processing data: ${e.getMessage}")
  //  }

  // generation of (future of) (validated) dataframe as string for web api

  // def convertDummyDfAsString0(session: SparkSession): String =
  //  val result = makeDummyDf0(session).dfToString
  //  result

  def convertDummyDfValidatedToString(
      session: SparkSession,
      schema: StructType,
      data: Seq[Row]
  ): Either[String, String] =
    val result = makeDummyDfValidated(session, schema, data).map(validatedDf =>
      validatedDf.dfToString
    )
    result

  // def convertDummyDfAsString2(session: SparkSession): Future[Either[String, String]] =
  //  val result = makeDummyDf2(session).map(validatedDf => validatedDf.map(df => df.dfToString))
  //  result

  // generation of (future of) (validated) expressions

  def makeDfExpressionNonValidatedMix(df: DataFrame): Expression[DataFrame] =
    // val dfDummy: Dataset[Row] = makeDummyDf()
    val dfExpression: Expression[DataFrame] =
      Mult(Plus(Num(df), Num(df)), Num(df)) // OK
      // Mult(Plus(Num(df), Num(df)), Plus(Num(df), Num(df))) //OK
      // Plus(Num(df), Num(df)) //OK
      // Mult(Num(df), Num(df)) //OK
      // Num(df) //OK
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

  // def runMakeExpression1(session: SparkSession): Either[String, Expression[DataFrame]] =
  //  val result = makeDummyDf1(session).map(validatedDf =>
  //    val expression: Expression[DataFrame] = makeExpression(validatedDf)
  //    expression)
  //  result

  // def runMakeExpression2(session: SparkSession): Future[Either[String, Expression[DataFrame]]] =
  //  val result = makeDummyDf2(session).map(validatedDf => validatedDf.map(df =>
  //    val expression: Expression[DataFrame] = makeExpression(df)
  //    expression))
  //  result

  // generation of (future of) validated expressions as string for web api

  // def runConvertExpressionAsString(): Future[Either[String, String]] = ???
  //  //val result = runMakeExpression().map(x => x.map(y => y.expressionToString))
  //  //result
end SparkJob