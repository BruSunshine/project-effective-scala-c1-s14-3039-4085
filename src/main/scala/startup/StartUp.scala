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
import upickle.default.{ReadWriter => RW, macroRW}

/** A dummy class to test project setup */
case class myStart(param: Int):

  /** A dummy value */
  val startvalue: String = "test"

  /** @param two integers to add */
  /** @return the addition of the 2 inputs */
  def add(x: Int, y: Int): Int = x + y

object myStart:
  implicit val rw: RW[myStart] = macroRW
/*
object myStart:
  implicit val rw: ReadWriter[myStart] = readwriter[ujson.Value].bimap[myStart](
    myStartInstance => writeJs(myStartInstance), // Serialize to JSON
    json => { // Deserialize from JSON
      val param = json.obj("param").num.toInt//toString.toInt // Explicitly convert to Int
      myStart(param)
    }
  )
*/




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
      case Num[T](num) => num
      case Plus[T](a, b) => ops.add(evaluate(a), evaluate(b))
      case Mult[T](a, b) => ops.mul(evaluate(a), evaluate(b))
  end evaluate

/*
import upickle.default.{writeJs, write}
import upickle.default.Writer

implicit def exprWriter : String =
  this match
    case Num(value) => write(value)
    case Plus(left, right) => write("Plus", exprWriter(left), exprWriter(right))
    case Mult(left, right) => write("Mult", exprWriter(left), exprWriter(right))
    */