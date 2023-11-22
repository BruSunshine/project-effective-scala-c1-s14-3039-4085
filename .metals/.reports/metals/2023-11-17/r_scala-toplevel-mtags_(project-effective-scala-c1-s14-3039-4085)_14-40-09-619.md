id: file://<WORKSPACE>/src/main/scala/startup/StartUp.scala:[1074..1078) in Input.VirtualFile("file://<WORKSPACE>/src/main/scala/startup/StartUp.scala", "package startup

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


sealed trait Expression:
  def evaluate(x: Expression): Int =
    x match
      case Num(num) => num
      case Plus(a, b) => evaluate(a) + evaluate(b)
      case Mult(a, b) => evaluate(a) * evaluate(b)

case class Plus(a: Num, b: Num) extends Expression

case class Mult(a: Num, b: Num) extends Expression

case class 

case class Num(n: Int) extends Expression")
file://<WORKSPACE>/src/main/scala/startup/StartUp.scala
file://<WORKSPACE>/src/main/scala/startup/StartUp.scala:53: error: expected identifier; obtained case
case class Num(n: Int) extends Expression
^
#### Short summary: 

expected identifier; obtained case