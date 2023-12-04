package sparkjobs

import org.apache.spark.sql.types._
import org.apache.spark.sql.{Row}

import app.SparkMain.sparkSession
import SparkJob.{runMakeExpression0, runMakeExpression1}

object DataFramesExemples:
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

  // Test dataframes
  //val df1 = runMakeDummyDf(sparkSession)
  //val df2 = ???
  //val df3 = ???

end DataFramesExemples

 
object ExpressionsExemples:
  // Test validated expressions
  val dfExpression1 = runMakeExpression0(sparkSession)

  // Test simple expressions
  val dfExpression = runMakeExpression1(sparkSession)

end ExpressionsExemples
