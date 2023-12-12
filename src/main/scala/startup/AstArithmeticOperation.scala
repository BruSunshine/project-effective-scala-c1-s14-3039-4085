package startup.ast

import org.apache.spark.sql.{Dataset, Row}
import org.apache.spark.sql.functions.{col, concat}

/*
trait OperationValidator[T]:
  def validate(t: T): Boolean
object OperationValidator:
  given OperationValidator[Int] with
    def validate(x: Int): Boolean = x >= 0
  given OperationValidator[Double] with
    def validate(x: Double): Boolean = x >= 0
  given OperationValidator[Dataset[Row]] with
    def validate(df: Dataset[Row]): Boolean = df.count < 1000
*/

/** A type class that defines arithmetic operations for a type `T`.
  */
trait ArithmeticOperation[T]:
  /** Adds two values of type `T`.
    */
  def add(x: T, y: T): T

  /** Multiplies two values of type `T`.
    */
  def mul(x: T, y: T): T

/** Companion object for `ArithmeticOperation` that provides given instances for
  * `Int` and `Double`.
  */
object ArithmeticOperation:

  /** Given instance of `ArithmeticOperation` for `Int`.
    */
  given IntOps: ArithmeticOperation[Int] with
    def add(x: Int, y: Int): Int =
      require((x < 50) && (y < 50), "Integers arguments are > threshold in add arithmetic operation")
      x + y
    def mul(x: Int, y: Int): Int = x * y

  /** Given instance of `ArithmeticOperation` for `Double`.
    */
  given DoubleOps: ArithmeticOperation[Double] with
    def add(x: Double, y: Double): Double = x + y
    def mul(x: Double, y: Double): Double = x * y

  /** Given instance of `ArithmeticOperation` for `DataFrame`.
    */
  given DfOps: ArithmeticOperation[Dataset[Row]] with
    def add(dfx: Dataset[Row], dfy: Dataset[Row]): Dataset[Row] =
      val resultDf =
        dfx
          .alias("dfx")
          .join(dfy.alias("dfy"), "index")
          .select(
            (col("dfx.index")).as("index"),
            (col("dfx.doubleField") + col("dfy.doubleField")).as("doubleField"),
            concat(col("dfx.stringField"), col("dfy.stringField"))
              .as("stringField"),
            (col("dfx.intField") + col("dfy.intField")).as("intField")
          )
      resultDf

    def mul(dfx: Dataset[Row], dfy: Dataset[Row]): Dataset[Row] =
      require(!dfx.isEmpty, "dataframes used in multiplication should be non-empty")
      require(!dfy.isEmpty, "dataframes used in multiplication should be non-empty")
      val resultDf =
        dfx
          .alias("dfx")
          .join(dfy.alias("dfy"), "index")
          .select(
            (col("dfx.index")).as("index"),
            (col("dfx.doubleField") * col("dfy.doubleField")).as("doubleField"),
            concat(
              col("dfx.stringField"),
              col("dfx.stringField"),
              col("dfy.stringField"),
              col("dfy.stringField")
            )
              .as("stringField"),
            (col("dfx.intField") * col("dfy.intField")).as("intField")
          )
      resultDf

