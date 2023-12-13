package startup.unit_testing

import munit.FunSuite
import startup.ast.{Expression, Num, Plus, Mult}
import sparkjobs.DataFramesExemples

class ExpressionSuite extends munit.FunSuite:

  test("validateExpression should validate a valid int expression") {
    val expr = Plus(Num(4), Mult(Num(6), Num(10)))
    assert(Expression.validateExpression(expr).isRight)
  }

  test("validateExpression should invalidate an invalid int expression") {
    val invalidExpr = Plus(Num(-1), Mult(Num(2), Num(7)))
    assert(Expression.validateExpression(invalidExpr).left.get.length == 2)
  }

  test("validateExpression should validate a valid double expression") {
    val expr = Plus(Num(4.0), Mult(Num(6.0), Num(10.0)))
    assert(Expression.validateExpression(expr).isRight)
  }

  test("validateExpression should invalidate an invalid double expression") {
    val invalidExpr = Plus(Num(-0.1), Mult(Num(0.1), Num(7.7)))
    assert(Expression.validateExpression(invalidExpr).left.get.length == 1)
  }

  test("validateExpression should validate a valid dataframe expression") {
    val expr = Plus(
      Num(DataFramesExemples.df1),
      Mult(Num(DataFramesExemples.df2), Num(DataFramesExemples.df3))
    )
    assert(Expression.validateExpression(expr).isRight)
  }

  test("validateExpression should invalidate an invalid dataframe expression") {
    val invalidExpr = Num(DataFramesExemples.df4)
    assert(Expression.validateExpression(invalidExpr).left.get.length == 1)
  }

  test("evaluateExpression should evaluate a valid int operation") {
    val validOperation = Plus(Num(6), Mult(Num(30), Num(7)))
    val validOperationValidated = Expression.validateExpression(validOperation)
    val validOperationEvaluated =
      Expression.evaluateValidExpression(validOperationValidated)
    assert(validOperationEvaluated.isRight)
  }

  test("evaluateExpression should invalidate an invalid int operation") {
    val validOperation = Plus(Num(600), Mult(Num(43), Num(7)))
    val validOperationValidated = Expression.validateExpression(validOperation)
    val validOperationEvaluated =
      Expression.evaluateValidExpression(validOperationValidated)
    assert(validOperationEvaluated.left.get.length == 1)
  }

  test("evaluateExpression should evaluate a valid dataframe operation") {
    val df = DataFramesExemples.df1
    val validOperation = Plus(Num(df), Mult(Num(df), Num(df)))
    val validOperationValidated = Expression.validateExpression(validOperation)
    val validOperationEvaluated =
      Expression.evaluateValidExpression(validOperationValidated)
    assert(validOperationEvaluated.isRight)
  }

  test("evaluateExpression should invalidate an invalid dataframe operation") {
    val dfa = DataFramesExemples.df1
    val dfb = DataFramesExemples.df3
    val validOperation = Plus(Num(dfa), Mult(Num(dfa), Num(dfb)))
    val validOperationValidated = Expression.validateExpression(validOperation)
    val validOperationEvaluated =
      Expression.evaluateValidExpression(validOperationValidated)
    assert(validOperationEvaluated.left.get.length == 1)
  }
end ExpressionSuite
