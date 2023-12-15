package startup.ast

import org.apache.spark.sql.{Dataset, Row}
import org.apache.spark.sql.functions.{col, concat}

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
    * Defines addition and multiplication for `Int`.
    */
  given IntOps: ArithmeticOperation[Int] with
    def add(x: Int, y: Int): Int = x + y
    def mul(x: Int, y: Int): Int = x * y

  /** Given instance of `ArithmeticOperation` for `Double`.
    * Defines addition and multiplication for `Double`.
    */
  given DoubleOps: ArithmeticOperation[Double] with
    def add(x: Double, y: Double): Double = x + y
    def mul(x: Double, y: Double): Double = x * y

  /** Given instance of `ArithmeticOperation` for `DataFrame`.
    * Defines addition and multiplication for `DataFrame`.
    * Addition concatenates string fields and adds numeric fields.
    * Multiplication concatenates string fields twice and multiplies numeric fields.
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

// Trait defining a validation operation on two instances of a type T.
trait OperationValidator[T]:
  def validate(t1: T, t2: T, op: String): Boolean
object OperationValidator:
  // Validator for operations on Ints.
  given OperationValidator[Int] with
    // Validates an operation between two Ints.
    // For "add", both Ints should be less than 800.
    // For "mul", both Ints should be less than 40.
    // Returns false for all other operations.
    def validate(x: Int, y: Int, op: String): Boolean =
      op match
        case "add" => (x < 800) && (y < 800)
        case "mul" => (x < 40) && (y < 40)
        case _     => false
  // Validator for operations on Doubles. All operations are valid.
  given OperationValidator[Double] with
    def validate(x: Double, y: Double, op: String): Boolean =
      true
  // Validator for operations on Datasets.
  given OperationValidator[Dataset[Row]] with
    // Validates an operation between two Datasets.
    // For "add" and "mul", both Datasets should have the same schema and not be empty.
    // Returns false for all other operations.
    def validate(df1: Dataset[Row], df2: Dataset[Row], op: String): Boolean =
      op match
        case "add" | "mul" => (df1.schema == df2.schema) && !df1.isEmpty && !df2.isEmpty
        case _     => false
