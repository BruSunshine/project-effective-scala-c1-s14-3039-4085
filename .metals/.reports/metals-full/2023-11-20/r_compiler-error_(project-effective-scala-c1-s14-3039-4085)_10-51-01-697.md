file://<WORKSPACE>/src/main/scala/startup/StartUp.scala
### java.lang.AssertionError: assertion failed: position error, parent span does not contain child span
parent      =  extends ArithmeticOperation[T] {
  def add(x: Int, y: Int): Int = x + null
} # -1,
parent span = <3299..3365>,
child       = def add(x: Int, y: Int): Int = x + null # -1,
child span  = [3331..3335..3366]

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

/** A dummy class to test project setup */
case class myStart(param: Int):

  /** A dummy value */
  val startvalue: String = "test"

  /** @param two integers to add */
  /** @return the addition of the 2 inputs */
  def add(x: Int, y: Int): Int = x + y

end myStart

/*
/** A class that simulates a stateful HTTP server */
class HttpServer:

  /** Start the HTTP server on the TCP port */
  def start(port: Int): Unit = ()

  /** Stop the HTTP server */
  def stop(): Unit = ()

end HttpServer
*/


//trait Arithmetic[T]:
//  def add(x: T, y: T): T
//  def mul(x: T, y: T): T
/*
sealed trait ArithmeticOperations[T]:

  //def add(a: T, b: T): T
  
  //def mult(a: T, b: T): T
  
  def evaluate(using ArithmeticOperations[T]): T =
    this match
      case Num[T](num) => num
      case Plus[T](a, b) => add(a.evaluate, b.evaluate)
      case Mult[T](a, b) => mult(a.evaluate, b.evaluate)
  end evaluate

case class Num[T](n: T) extends ArithmeticOperations[T]

case class Mult[T](a: ArithmeticOperations[T], b: ArithmeticOperations[T]) extends ArithmeticOperations[T]:
  def mult(a: T, b: T): T
object Mult:
  given IntMult: ArithmeticOperations[Int] with
    def mult(a: Int, b: Int): Int = a * b
  given DoubleMult: ArithmeticOperations[Double] with
    def mult(a: Double, b: Double): Double = a * b

case class Plus[T](a: ArithmeticOperations[T], b: ArithmeticOperations[T]) extends ArithmeticOperations[T]:
  def add(a: T, b: T): T
object Plus:
  given IntPlus: ArithmeticOperations[Int] with
    def add(a: Int, b: Int): Int = a + b
  given DoublePlus: ArithmeticOperations[Double] with
    def add(a: Double, b: Double): Double = a + b

*/


/*
abstract case class Num[T](n: T) extends ArithmeticOperations[T]

abstract case class Mult[T](a: ArithmeticOperations[T], b: ArithmeticOperations[T]) extends ArithmeticOperations[T]
//object Mult:
//  given IntMult: ArithmeticOperations[Int] with
//    def mult(a: Int, b: Int): Int = a * b
//  given DoubleMult: ArithmeticOperations[Double] with
//    def mult(a: Double, b: Double): Double = a * b

abstract case class Plus[T](a: ArithmeticOperations[T], b: ArithmeticOperations[T]) extends ArithmeticOperations[T]
//object Plus:
//  given IntPlus: ArithmeticOperations[Int] with
//    def add(a: Int, b: Int): Int = a + b
//  given DoublePlus: ArithmeticOperations[Double] with
//    def add(a: Double, b: Double): Double = a + b

object ArithmeticOperations:
  given IntOps: ArithmeticOperations[Int] with
    def mult(a: Int, b: Int): Int = a * b
    def add(a: Int, b: Int): Int = a + b
  given DoubleOps: ArithmeticOperations[Double] with
    def mult(a: Double, b: Double): Double = a * b
    def add(a: Double, b: Double): Double = a + b
  //given IntPlus: ArithmeticOperations[Int] with
  //  def add(a: Int, b: Int): Int = a + b
  //given DoublePlus: ArithmeticOperations[Double] with
  //  def add(a: Double, b: Double): Double = a + b
*/


//

trait ArithmeticOperation[T]:
  def add(x: Int, y: Int): Int
  def mul(x: Int, y: Int): Int
object ArithmeticOperation:
  given IntOps: ArithmeticOperation[T] with
    def add(x: Int, y: Int): Int = x + 
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
parent      =  extends ArithmeticOperation[T] {
  def add(x: Int, y: Int): Int = x + null
} # -1,
parent span = <3299..3365>,
child       = def add(x: Int, y: Int): Int = x + null # -1,
child span  = [3331..3335..3366]