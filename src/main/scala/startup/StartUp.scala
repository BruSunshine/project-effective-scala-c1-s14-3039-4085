package startup

// This file contains ...

/** Interface for the project setup structure
  *
  * This interface defines the operations for constructing and manipulating ...
  * modeled like this, works like this
  *
  * @see
  *   http://blabla
  */
//import upickle.default.{read, readwriter, writeJs, RW, macroR, macroRW, Reader, ReadWriter}
import upickle.default.{ReadWriter => RW, macroRW, Reader, Writer, reader, writer}
import org.apache.spark.sql.{DataFrame, Dataset, Row}
import org.apache.spark.sql.functions.{col, concat, lit}

/** A dummy class to test project setup */
case class myStart(param: Int):

  /** A dummy value */
  val startvalue: String = "test"

  /** @param two integers to add */
  /** @return the addition of the 2 inputs */
  def add(x: Int, y: Int): Int = x + y

object myStart:
  given rw: RW[myStart] = macroRW


case class Arg(arg: startup.myStart)
object Arg:
  given argRW: RW[Arg] = macroRW


/**
 * A type class that defines arithmetic operations for a type `T`.
 */
trait ArithmeticOperation[T]:
  /**
   * Adds two values of type `T`.
   */
  def add(x: T, y: T): T
  /**
   * Multiplies two values of type `T`.
   */
  def mul(x: T, y: T): T
/**
 * Companion object for `ArithmeticOperation` that provides given instances for `Int` and `Double`.
 */
object ArithmeticOperation:
  
  /**
   * Given instance of `ArithmeticOperation` for `Int`.
   */
  given IntOps: ArithmeticOperation[Int] with
    def add(x: Int, y: Int): Int = x + y
    def mul(x: Int, y: Int): Int = x * y
  /**
   * Given instance of `ArithmeticOperation` for `Double`.
   */
  given DoubleOps: ArithmeticOperation[Double] with
    def add(x: Double, y: Double): Double = x + y
    def mul(x: Double, y: Double): Double = x * y
  /**
   * Given instance of `ArithmeticOperation` for `DataFrame`.
   */
  given DfOps: ArithmeticOperation[Dataset[Row]] with
    def add(dfx: Dataset[Row], dfy: Dataset[Row]): Dataset[Row] =
      val resultDf =
        dfx.alias("dfx")
        .join(dfy.alias("dfy"), Seq("doubleField", "stringField", "intField"))
        .select(
          (col("dfx.doubleField") + col("dfy.doubleField")).as("doubleField"),
          concat(col("dfx.stringField"), col("dfy.stringField")).as("stringField"),
          (col("dfx.intField") + col("dfy.intField")).as("intField")
          )
      resultDf

    def mul(dfx: Dataset[Row], dfy: Dataset[Row]): Dataset[Row] =
      val resultDf =
        dfx.alias("dfx")
        .join(dfy.alias("dfy"), Seq("doubleField", "stringField", "intField"))
        .select(
          (col("dfx.doubleField") + col("dfy.doubleField")).as("doubleField"),
          concat(col("dfx.stringField"), col("dfy.stringField")).as("stringField"),
          (col("dfx.intField") + col("dfy.intField")).as("intField")
          )
      resultDf

/**
 * A sealed trait representing an arithmetic expression.
 */
sealed trait Expression[T]
/**
 * A case class representing a number in an arithmetic expression.
 */
case class Num[T](n: T) extends Expression[T]
/**
 * A case class representing a multiplication operation in an arithmetic expression.
 */
case class Mult[T](a: Expression[T], b: Expression[T]) extends Expression[T]
/**
 * A case class representing an addition operation in an arithmetic expression.
 */
case class Plus[T](a: Expression[T], b: Expression[T]) extends Expression[T]

/**
 * Companion object for `Expression` that provides a method to evaluate an expression.
 */
object Expression:
  
  /**
   * Evaluates an arithmetic expression.
   *
   * @param expression The expression to evaluate.
   * @param ops The given instance of `ArithmeticOperation` for the type `T`.
   * @return The result of the evaluation.
   */
  def evaluate[T](expression: Expression[T])(using ops: ArithmeticOperation[T]): T =
    expression match
      case Mult[T](a, b) => ops.mul(evaluate(a), evaluate(b))
      case Plus[T](a: Mult[T], b) => ops.add(evaluate(a), evaluate(b))
      case Plus[T](a, b: Mult[T]) => ops.add(evaluate(a), evaluate(b))
      case Plus[T](a, b) => ops.add(evaluate(a), evaluate(b))
      case Num[T](num) => num
      
  end evaluate

  given [T](using RW[T]): RW[Expression[T]] = RW.merge(
    macroRW[Num[T]],
    macroRW[Plus[T]],
    macroRW[Mult[T]]
  )

case class ArgAst[T](argast: Expression[T])
object ArgAst:
  //given argAstRWInt: RW[ArgAst[Int]] = macroRW
  //given argAstRWDouble: RW[ArgAst[Double]] = macroRW
  //given argAstRWDf: RW[ArgAst[Dataset[Row]]] = macroRW[Dataset[Row]]
  given [T](using RW[T]): RW[ArgAst[T]] = macroRW[ArgAst[T]]
  

  //given [T: Writer]: Writer[ArgAst[T]] with
  //  def write0[V](out: Visitor[?, V], v: ArgAst[T]): V = summon[Writer[T]].write0(out, v.expr)

  
  //given [T: Reader: Writer]: RW[ArgAst[T]] with
  //  def read: Reader[ArgAst[T]] = reader[Expression[T]].map(ArgAst(_))
  //  def write: Writer[ArgAst[T]] = writer[Expression[T]].comap(_.argast)
