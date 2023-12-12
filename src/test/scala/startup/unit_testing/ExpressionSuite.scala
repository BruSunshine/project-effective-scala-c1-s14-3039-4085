package startup.unit_testing

import munit.FunSuite
import startup.ast.{Expression, Num, Plus, Mult}
import sparkjobs.DataFramesExemples

class ExpressionSuite extends munit.FunSuite:
/*  
  test("validateExpression should validate a valid int expression") {
    val expr = Plus(Num(4), Mult(Num(6), Num(10)))
    assert(Expression.validateExpression1(expr).isRight)
  }

  test("validateExpression should invalidate an invalid int expression") {
    val invalidExpr = Plus(Num(-1), Mult(Num(2), Num(7)))
    assert(Expression.validateExpression1(invalidExpr).left.get.length == 2)
  }
*/
  //test("validateExpression should invalidate an invalid int operation") {
  //  val invalidExpr = Plus(Num(-1), Mult(Num(65), Num(7)))
  //  val invalidExprEvaluated = Expression.evaluate(invalidExpr)
  //  assert(invalidExprEvaluated.left.get.length == 2)
  //}
/*  
  test("validateExpression should validate a valid double expression") {
    val expr = Plus(Num(4.0), Mult(Num(6.0), Num(10.0)))
    assert(Expression.validateExpression1(expr).isRight)
  }

  test("validateExpression should invalidate an invalid double expression") {
    val invalidExpr = Plus(Num(-0.1), Mult(Num(0.1), Num(7.7)))
    assert(Expression.validateExpression1(invalidExpr).left.get.length == 1)
  }

  test("validateExpression should validate a valid dataframe expression") {
    val expr = Plus(Num(DataFramesExemples.df1), Mult(Num(DataFramesExemples.df2), Num(DataFramesExemples.df3)))
    assert(Expression.validateExpression1(expr).isRight)
  }

  test("validateExpression should invalidate an invalid dataframe expression") {
    val invalidExpr = Num(DataFramesExemples.df4)
    assert(Expression.validateExpression1(invalidExpr).left.get.length == 1)
  }
*/  
end ExpressionSuite
