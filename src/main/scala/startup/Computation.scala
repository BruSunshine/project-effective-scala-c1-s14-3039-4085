package startup.computation

import startup.ast._

def runComputation(): String =
  val myStartinstance = myStart(0)
  val myvalue = myStartinstance.add(2, 3)
  s"let's start ${myStartinstance.startvalue} with value $myvalue"

def runComputation2(): String =
  val x: Int = 1
  val y: Int = 5
  val z: Int = 7
  val expr: Expression[Int] = Mult(Plus(Num(x), Num(y)), Num(z))
  val res = Expression.evaluate(expr)
  s"evaluation of expression $expr with parameters $x and $y and $z is $res"
