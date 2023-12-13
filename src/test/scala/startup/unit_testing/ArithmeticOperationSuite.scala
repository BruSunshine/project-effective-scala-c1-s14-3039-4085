package startup.unit_testing

import munit.FunSuite
import startup.ast.ArithmeticOperation
import startup.ast.{Expression, Num, Plus, Mult}
import sparkjobs.DataFramesExemples
import startup.ast.DataFrameName.contentHash

class ArithmeticOperationSuite extends munit.FunSuite:

  test("Evaluation of integer number expression is correct") {
    val ObtainedValidatedIntExpressionNum =
      Expression.validateExpression(Num(6))
    val ObtainedEvalIntExpressionNum =
      Expression.evaluateValidExpression(ObtainedValidatedIntExpressionNum)
    val ExpectedEvalIntExpressionNum = Right(6)
    assertEquals(ObtainedEvalIntExpressionNum, ExpectedEvalIntExpressionNum)
  }

  test("Evaluation of complex integer expression is correct") {
    val ObtainedValidatedIntExpressionMix =
      Expression.validateExpression(Mult(Plus(Num(6), Num(6)), Num(5)))
    val ObtainedEvalIntExpressionMix =
      Expression.evaluateValidExpression(ObtainedValidatedIntExpressionMix)
    val ExpectedEvalIntExpressionMix = Right(60)
    assertEquals(ObtainedEvalIntExpressionMix, ExpectedEvalIntExpressionMix)
  }

  test("Evaluation of integer addition expression is correct") {
    val ObtainedValidatedIntExpressionPlus =
      Expression.validateExpression(Plus(Num(10), Num(12)))
    val ObtainedEvalIntExpressionPlus =
      Expression.evaluateValidExpression(ObtainedValidatedIntExpressionPlus)
    val ExpectedEvalIntExpressionPlus = Right(22)
    assertEquals(ObtainedEvalIntExpressionPlus, ExpectedEvalIntExpressionPlus)
  }

  test("Evaluation of complex integer invalid operation is invalidated") {
    val ObtainedInvalidIntOpMix =
      Expression.validateExpression(Mult(Plus(Num(850), Num(6)), Num(50)))
    val ObtainedEvalIntExpressionMix =
      Expression.evaluateValidExpression(ObtainedInvalidIntOpMix)
    val ExpectedEvalIntExpressionMix =
      List("java.lang.Exception: Invalid plus operation")
    assertEquals(
      ObtainedEvalIntExpressionMix.left.get,
      ExpectedEvalIntExpressionMix
    )
  }

  test("Evaluation of double number expression is correct") {
    val ObtainedValidatedDoubleExpressionNum =
      Expression.validateExpression(Num(3.6))
    val ObtainedEvalDoubleExpressionNum =
      Expression.evaluateValidExpression(ObtainedValidatedDoubleExpressionNum)
    val ExpectedEvalDoubleExpressionNum = Right(3.6)
    val delta: Double = 0.0001
    assertEquals(
      ObtainedEvalDoubleExpressionNum,
      ExpectedEvalDoubleExpressionNum,
      delta
    )
  }

  test("Evaluation of complex double expression is correct") {
    val ObtainedValidatedDoubleExpressionMix =
      Expression.validateExpression(Mult(Plus(Num(3.8), Num(4.5)), Num(5.9)))
    val ObtainedEvalDoubleExpressionMix =
      Expression.evaluateValidExpression(ObtainedValidatedDoubleExpressionMix)
    val ExpectedEvalDoubleExpressionMix = Right(48.97)
    val precision = 2
    val roundedObtained =
      ObtainedEvalDoubleExpressionMix
        .map(x =>
          BigDecimal(x)
            .setScale(precision, BigDecimal.RoundingMode.HALF_UP)
            .toDouble
        )
        .right
        .get
    val roundedExpected = ExpectedEvalDoubleExpressionMix
      .map(x =>
        BigDecimal(x)
          .setScale(precision, BigDecimal.RoundingMode.HALF_UP)
          .toDouble
      )
      .right
      .get
    assertEquals(
      roundedObtained,
      roundedExpected
    )
  }

  test("Evaluation of double addition expression is correct") {
    val ObtainedValidatedDoubleExpressionPlus =
      Expression.validateExpression(Plus(Num(3.7), Num(4.6)))
    val ObtainedEvalDoubleExpressionPlus =
      Expression.evaluateValidExpression(ObtainedValidatedDoubleExpressionPlus)
    val ExpectedEvalDoubleExpressionPlus = Right(8.3)
    val delta: Double = 0.0001
    assertEquals(
      ObtainedEvalDoubleExpressionPlus,
      ExpectedEvalDoubleExpressionPlus,
      delta
    )
  }

  test("Evaluation of Dataset number expression is correct") {
    val ObtainedValidatedDfExpressionNum =
      Expression.validateExpression(Num(DataFramesExemples.df1))
    val ObtainedEvalDfExpressionNum =
      Expression.evaluateValidExpression(ObtainedValidatedDfExpressionNum)
    val ExpectedEvalDfExpressionNum = Right(DataFramesExemples.df1)
    assertEquals(
      ObtainedEvalDfExpressionNum.map(x => x.contentHash),
      ExpectedEvalDfExpressionNum.map(x => x.contentHash)
    )
  }

  test("Evaluation of complex Dataset expression is correct") {
    val ObtainedValidatedDfExpressionMix =
      Expression
        .validateExpression(
          Mult(
            Plus(Num(DataFramesExemples.df1), Num(DataFramesExemples.df1)),
            Num(DataFramesExemples.df1)
          )
        )
    val ObtainedEvalDfExpressionMix =
      Expression
        .evaluateValidExpression(ObtainedValidatedDfExpressionMix)
        .map(x => x.sort("index"))
    val ExpectedEvalDfExpressionMix =
      Right(DataFramesExemples.df2.sort("index"))
    assertEquals(
      ObtainedEvalDfExpressionMix.map(x => x.contentHash),
      ExpectedEvalDfExpressionMix.map(x => x.contentHash)
    )
  }

  test("Evaluation of Dataset addition expression is correct") {
    val ObtainedValidatedDfExpressionPlus =
      Expression.validateExpression(
        Plus(Num(DataFramesExemples.df1), Num(DataFramesExemples.df1))
      )
    val ObtainedEvalDfExpressionPlus =
      Expression.evaluateValidExpression(ObtainedValidatedDfExpressionPlus)
    val ExpectedEvalDfExpressionPlus = Right(DataFramesExemples.df3)
    assertEquals(
      ObtainedEvalDfExpressionPlus.map(x => x.contentHash),
      ExpectedEvalDfExpressionPlus.map(x => x.contentHash)
    )
  }

  test("Evaluation of complex dataframe invalid operation is invalidated") {
    val ObtainedInvalidDfOpMix =
      Expression.validateExpression(
        Mult(
          Plus(Num(DataFramesExemples.df1), Num(DataFramesExemples.df2)),
          Num(DataFramesExemples.df3)
        )
      )
    val ObtainedEvalDfExpressionMix =
      Expression.evaluateValidExpression(ObtainedInvalidDfOpMix)
    val ExpectedEvalDfExpressionMix =
      List("java.lang.Exception: Invalid mul operation")
    assertEquals(
      ObtainedEvalDfExpressionMix.left.get,
      ExpectedEvalDfExpressionMix
    )
  }

end ArithmeticOperationSuite
