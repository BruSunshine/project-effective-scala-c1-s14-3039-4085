package startup.ast

import org.apache.spark.sql.{Dataset, Row}
import org.apache.spark.sql.functions.{col, concat}

trait OperationValidator[T]:
  def validate(t1: T, t2: T, op: String): Boolean
object OperationValidator:
  given OperationValidator[Int] with
    def validate(x: Int, y: Int, op: String): Boolean =
      op match
        case "add" => (x < 800) && (y < 800)
        case "mul" => (x < 40) && (y < 40)
        case _     => false
  given OperationValidator[Double] with
    def validate(x: Double, y: Double, op: String): Boolean =
      true
  given OperationValidator[Dataset[Row]] with
    def validate(df1: Dataset[Row], df2: Dataset[Row], op: String): Boolean =
      op match
        case "add" => (df1.schema == df2.schema) && !df1.isEmpty && !df2.isEmpty
        case "mul" => (df1.schema == df2.schema) && !df1.isEmpty && !df2.isEmpty
        case _     => false

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
    def add(x: Int, y: Int): Int = x + y
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
