


/*
import $ivy.`com.lihaoyi::cask:0.9.1`
import $ivy.`com.lihaoyi::upickle:3.1.3`
import $ivy.`com.lihaoyi::requests:0.8.0`
import requests._
import io.undertow.Undertow
import App._

val server = Undertow.builder
        .addHttpListener(8080, "localhost")
        .setHandler(app.MinimalRoutesMain.defaultHandler)
        .build
        
server.start()

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
