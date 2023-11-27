file://<WORKSPACE>/src/main/scala/startup/StartUp.scala
### java.lang.StringIndexOutOfBoundsException: begin 3073, end 3080, length 3075

occurred in the presentation compiler.

action parameters:
offset: 3073
uri: file://<WORKSPACE>/src/main/scala/startup/StartUp.scala
text:
```scala
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

  given [T](using rw: RW[T]): RW[Expression[T]] = RW.merge(
    macroRW[Num[T]],
    macroRW[Plus[T]],
    macroRW[Mult[T]]
  )

case class ArgAst[T](argast: Expression[T])
object ArgAst:
  //given argAstRWInt: RW[ArgAst[Int]] = macroRW
  //given argAstRWDouble: RW[ArgAst[Double]] = macroRW
  given @@T

```



#### Error stacktrace:

```
java.base/java.lang.String.checkBoundsBeginEnd(String.java:3319)
	java.base/java.lang.String.substring(String.java:1874)
	scala.meta.internal.pc.PcCollector.isGeneratedGiven(PcCollector.scala:175)
	scala.meta.internal.pc.PcCollector.soughtSymbols(PcCollector.scala:229)
	scala.meta.internal.pc.PcCollector.resultWithSought(PcCollector.scala:374)
	scala.meta.internal.pc.PcCollector.result(PcCollector.scala:364)
	scala.meta.internal.pc.PcDocumentHighlightProvider.highlights(PcDocumentHighlightProvider.scala:32)
	scala.meta.internal.pc.ScalaPresentationCompiler.documentHighlight$$anonfun$1(ScalaPresentationCompiler.scala:155)
```
#### Short summary: 

java.lang.StringIndexOutOfBoundsException: begin 3073, end 3080, length 3075