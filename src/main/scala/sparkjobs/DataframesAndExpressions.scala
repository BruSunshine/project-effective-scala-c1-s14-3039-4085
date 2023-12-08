package sparkjobs

import org.apache.spark.sql.types._
import org.apache.spark.sql.{Row}
import app.SparkMain.sparkSession
import SparkJob.{
  makeDummyDfNonValidated,
  runMakeExpressionNonValidatedMix
}

object DataFramesExemples:

  val schema1: StructType = StructType(
    Array(
      StructField("index", IntegerType, nullable = false),
      StructField("doubleField", DoubleType, nullable = false),
      StructField("stringField", StringType, nullable = false),
      StructField("intField", IntegerType, nullable = false)
    )
  )

  val data1 = Seq(
    Row(1, 1.0, "a", 1),
    Row(2, 2.0, "b", 2),
    Row(3, 3.0, "c", 3),
    Row(4, 4.0, "d", 4),
    Row(5, 5.0, "e", 5)
  )

  val data2 = Seq(
    Row(1, 2.0, "aaaaaa", 2),
    Row(2, 8.0, "bbbbbb", 8),
    Row(3, 18.0, "cccccc", 18),
    Row(4, 32.0, "dddddd", 32),
    Row(5, 50.0, "eeeeee", 50)
  )

  val data3 = Seq(
    Row(1, 2.0, "aa", 2),
    Row(2, 4.0, "bb", 4),
    Row(3, 6.0, "cc", 6),
    Row(4, 8.0, "dd", 8),
    Row(5, 10.0, "ee", 10)
  )

  val df1 = makeDummyDfNonValidated(sparkSession, schema1, data1)
  val df2 = makeDummyDfNonValidated(sparkSession, schema1, data2)
  val df3 = makeDummyDfNonValidated(sparkSession, schema1, data3)

end DataFramesExemples

object ExpressionsExemples:

  val dfExpression0 = runMakeExpressionNonValidatedMix(
    sparkSession,
    DataFramesExemples.schema1,
    DataFramesExemples.data1
  )

end ExpressionsExemples
