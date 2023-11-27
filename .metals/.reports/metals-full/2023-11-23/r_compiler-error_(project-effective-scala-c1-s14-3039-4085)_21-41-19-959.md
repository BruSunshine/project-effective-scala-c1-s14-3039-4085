file://<WORKSPACE>/src/main/scala/startup/StartUp.scala
### java.lang.AssertionError: assertion failed: position error, parent span does not contain child span
parent      =  extends ArithmeticOperation[DataFrame] {
  def add(dfx: DataFrame, dfy: DataFrame): DataFrame = null
} # -1,
parent span = <1660..1820>,
child       = def add(dfx: DataFrame, dfy: DataFrame): DataFrame = null # -1,
child span  = [1702..1706..1887]

occurred in the presentation compiler.

action parameters:
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
import org.apache.spark.sql.{DataFrame}

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
  
    given DfOps: ArithmeticOperation[DataFrame] with
      def add(dfx: DataFrame, dfy: DataFrame): DataFrame = 
      def mul(dfx: DataFrame, dfy: DataFrame): DataFrame = x * y

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
  given [T](using RW[T]): RW[ArgAst[T]] = macroRW[ArgAst[T]]

```



#### Error stacktrace:

```
scala.runtime.Scala3RunTime$.assertFailed(Scala3RunTime.scala:8)
	dotty.tools.dotc.ast.Positioned.check$1(Positioned.scala:175)
	dotty.tools.dotc.ast.Positioned.check$1$$anonfun$3(Positioned.scala:205)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:15)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:10)
	scala.collection.immutable.List.foreach(List.scala:333)
	dotty.tools.dotc.ast.Positioned.check$1(Positioned.scala:205)
	dotty.tools.dotc.ast.Positioned.checkPos(Positioned.scala:226)
	dotty.tools.dotc.ast.Positioned.check$1(Positioned.scala:200)
	dotty.tools.dotc.ast.Positioned.checkPos(Positioned.scala:226)
	dotty.tools.dotc.ast.Positioned.check$1(Positioned.scala:200)
	dotty.tools.dotc.ast.Positioned.check$1$$anonfun$3(Positioned.scala:205)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:15)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:10)
	scala.collection.immutable.List.foreach(List.scala:333)
	dotty.tools.dotc.ast.Positioned.check$1(Positioned.scala:205)
	dotty.tools.dotc.ast.Positioned.checkPos(Positioned.scala:226)
	dotty.tools.dotc.ast.Positioned.check$1(Positioned.scala:200)
	dotty.tools.dotc.ast.Positioned.checkPos(Positioned.scala:226)
	dotty.tools.dotc.ast.Positioned.check$1(Positioned.scala:200)
	dotty.tools.dotc.ast.Positioned.check$1$$anonfun$3(Positioned.scala:205)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:15)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:10)
	scala.collection.immutable.List.foreach(List.scala:333)
	dotty.tools.dotc.ast.Positioned.check$1(Positioned.scala:205)
	dotty.tools.dotc.ast.Positioned.checkPos(Positioned.scala:226)
	dotty.tools.dotc.ast.Positioned.check$1(Positioned.scala:200)
	dotty.tools.dotc.ast.Positioned.checkPos(Positioned.scala:226)
	dotty.tools.dotc.ast.Positioned.check$1(Positioned.scala:200)
	dotty.tools.dotc.ast.Positioned.check$1$$anonfun$3(Positioned.scala:205)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:15)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:10)
	scala.collection.immutable.List.foreach(List.scala:333)
	dotty.tools.dotc.ast.Positioned.check$1(Positioned.scala:205)
	dotty.tools.dotc.ast.Positioned.checkPos(Positioned.scala:226)
	dotty.tools.dotc.parsing.Parser.parse$$anonfun$1(ParserPhase.scala:38)
	dotty.tools.dotc.parsing.Parser.parse$$anonfun$adapted$1(ParserPhase.scala:39)
	scala.Function0.apply$mcV$sp(Function0.scala:42)
	dotty.tools.dotc.core.Phases$Phase.monitor(Phases.scala:440)
	dotty.tools.dotc.parsing.Parser.parse(ParserPhase.scala:39)
	dotty.tools.dotc.parsing.Parser.runOn$$anonfun$1(ParserPhase.scala:48)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:15)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:10)
	scala.collection.immutable.List.foreach(List.scala:333)
	dotty.tools.dotc.parsing.Parser.runOn(ParserPhase.scala:48)
	dotty.tools.dotc.Run.runPhases$1$$anonfun$1(Run.scala:246)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:15)
	scala.runtime.function.JProcedure1.apply(JProcedure1.java:10)
	scala.collection.ArrayOps$.foreach$extension(ArrayOps.scala:1321)
	dotty.tools.dotc.Run.runPhases$1(Run.scala:262)
	dotty.tools.dotc.Run.compileUnits$$anonfun$1(Run.scala:270)
	dotty.tools.dotc.Run.compileUnits$$anonfun$adapted$1(Run.scala:279)
	dotty.tools.dotc.util.Stats$.maybeMonitored(Stats.scala:67)
	dotty.tools.dotc.Run.compileUnits(Run.scala:279)
	dotty.tools.dotc.Run.compileSources(Run.scala:194)
	dotty.tools.dotc.interactive.InteractiveDriver.run(InteractiveDriver.scala:165)
	scala.meta.internal.pc.MetalsDriver.run(MetalsDriver.scala:45)
	scala.meta.internal.pc.PcCollector.<init>(PcCollector.scala:42)
	scala.meta.internal.pc.PcSemanticTokensProvider$Collector$.<init>(PcSemanticTokensProvider.scala:60)
	scala.meta.internal.pc.PcSemanticTokensProvider.Collector$lzyINIT1(PcSemanticTokensProvider.scala:60)
	scala.meta.internal.pc.PcSemanticTokensProvider.Collector(PcSemanticTokensProvider.scala:60)
	scala.meta.internal.pc.PcSemanticTokensProvider.provide(PcSemanticTokensProvider.scala:81)
	scala.meta.internal.pc.ScalaPresentationCompiler.semanticTokens$$anonfun$1(ScalaPresentationCompiler.scala:99)
```
#### Short summary: 

java.lang.AssertionError: assertion failed: position error, parent span does not contain child span
parent      =  extends ArithmeticOperation[DataFrame] {
  def add(dfx: DataFrame, dfy: DataFrame): DataFrame = null
} # -1,
parent span = <1660..1820>,
child       = def add(dfx: DataFrame, dfy: DataFrame): DataFrame = null # -1,
child span  = [1702..1706..1887]