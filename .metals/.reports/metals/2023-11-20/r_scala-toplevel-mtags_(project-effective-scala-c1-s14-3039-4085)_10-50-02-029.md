id: file://<WORKSPACE>/src/main/scala/startup/StartUp.scala:[3262..3262) in Input.VirtualFile("file://<WORKSPACE>/src/main/scala/startup/StartUp.scala", "package startup

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
object ")
file://<WORKSPACE>/src/main/scala/startup/StartUp.scala
file://<WORKSPACE>/src/main/scala/startup/StartUp.scala:114: error: expected identifier; obtained eof
object 
       ^
#### Short summary: 

expected identifier; obtained eof