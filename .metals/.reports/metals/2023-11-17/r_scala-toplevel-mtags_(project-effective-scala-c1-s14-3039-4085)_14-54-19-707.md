id: file://<WORKSPACE>/src/main/scala/startup/StartUp.scala:[999..1003) in Input.VirtualFile("file://<WORKSPACE>/src/main/scala/startup/StartUp.scala", "package startup

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


sealed trait ArithmeticOperations[T]:
  def evaluate(x: ArithmeticOperations[T]): T =
    x match
      case Num[T](num) => num
      case Plus[T](a, b) => evaluate(a) + evaluate(b)
      case Mult[T](a, b) => evaluate(a) * evaluate(b)

object 

case class Plus[T](a: Num[T], b: Num[T]) extends ArithmeticOperations

case class Mult[T](a: Num[T], b: Num[T]) extends ArithmeticOperations

case class Num[T](n: T) extends ArithmeticOperations")
file://<WORKSPACE>/src/main/scala/startup/StartUp.scala
file://<WORKSPACE>/src/main/scala/startup/StartUp.scala:49: error: expected identifier; obtained case
case class Plus[T](a: Num[T], b: Num[T]) extends ArithmeticOperations
^
#### Short summary: 

expected identifier; obtained case