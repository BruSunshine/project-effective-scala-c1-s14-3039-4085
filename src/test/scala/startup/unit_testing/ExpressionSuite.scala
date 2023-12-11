package startup.unit_testing

import munit.FunSuite
//import startup.ast.ArithmeticOperation
import startup.ast.{Expression, Num, Plus, Mult}
//import org.apache.spark.sql.{Dataset, Row}
//import sparkjobs.DataFramesExemples
//import startup.ast.DataFrameName.contentHash

class ExpressionSuite extends munit.FunSuite:
  
  test("validateExpression should validate a valid expression") {
    val expr = Plus(Num(1), Mult(Num(2), Num(3)))
    assert(Expression.validateExpression(expr).isRight)
  }

  test("validateExpression should invalidate an invalid expression") {
    // Assuming that Mult(Num(0), Num(0)) is an invalid expression
    val expr = Plus(Num(1), Mult(Num(0), Num(0)))
    assert(Expression.validateExpression(expr).isLeft)
  }

end ExpressionSuite
