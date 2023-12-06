package startup.computation

import startup.ast._

def runComputation(): String =
  val myStartinstance = myStart(0)
  val myvalue = myStartinstance.add(2, 3)
  s"let's start ${myStartinstance.startvalue} with value $myvalue"

def runComputation2(x: Option[Int], y: Option[Int], z: Option[Int]): String =
  val expr: Expression[Int] = Mult(Plus(Num(x.get), Num(y.get)), Num(z.get))
  val res = Expression.evaluate(expr)
  s"evaluation of expression $expr with parameters $x and $y and $z is $res"