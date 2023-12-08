package startup.unit_testing

import munit.FunSuite
import startup.ast.ArithmeticOperation
import startup.ast.{Expression, Num, Plus, Mult}
import org.apache.spark.sql.{Dataset, Row}
import sparkjobs.DataFramesExemples
import startup.ast.DataFrameName.contentHash

class ArithmeticOperationSuite extends munit.FunSuite:

  test("Evaluation of integer number expression is correct") {
    val ObtainedEvalIntExpressionNum: Int = Expression.evaluate(Num(3))
    val ExpectedEvalIntExpressionNum: Int = 3
    assertEquals(ObtainedEvalIntExpressionNum, ExpectedEvalIntExpressionNum)
  }

  test("Evaluation of complex integer expression is correct") {
    val ObtainedEvalIntExpressionMix: Int =
      Expression.evaluate(Mult(Plus(Num(3), Num(4)), Num(5)))
    val ExpectedEvalIntExpressionMix: Int = 35
    assertEquals(ObtainedEvalIntExpressionMix, ExpectedEvalIntExpressionMix)
  }

  test("Evaluation of integer addition expression is correct") {
    val ObtainedEvalIntExpressionPlus: Int =
      Expression.evaluate(Plus(Num(3), Num(4)))
    val ExpectedEvalIntExpressionPlus: Int = 7
    assertEquals(ObtainedEvalIntExpressionPlus, ExpectedEvalIntExpressionPlus)
  }

  test("Evaluation of double number expression is correct") {
    val ObtainedEvalDoubleExpressionNum: Double = Expression.evaluate(Num(3.6))
    val ExpectedEvalDoubleExpressionNum: Double = 3.6
    val delta: Double = 0.0001
    assertEquals(
      ObtainedEvalDoubleExpressionNum,
      ExpectedEvalDoubleExpressionNum,
      delta
    )
  }

  test("Evaluation of complex double expression is correct") {
    val ObtainedEvalDoubleExpressionMix: Double =
      Expression.evaluate(Mult(Plus(Num(3.8), Num(4.5)), Num(5.9)))
    val ExpectedEvalDoubleExpressionMix: Double = 48.97
    // val delta: Double = 0.00000000000001
    val precision = 2
    val roundedObtained = BigDecimal(ObtainedEvalDoubleExpressionMix)
      .setScale(precision, BigDecimal.RoundingMode.HALF_UP)
      .toDouble
    val roundedExpected = BigDecimal(ExpectedEvalDoubleExpressionMix)
      .setScale(precision, BigDecimal.RoundingMode.HALF_UP)
      .toDouble
    assertEquals(
      roundedObtained,
      roundedExpected
      // ObtainedEvalDoubleExpressionMix,
      // ExpectedEvalDoubleExpressionMix,
      // delta
    )
  }

  test("Evaluation of double addition expression is correct") {
    val ObtainedEvalDoubleExpressionPlus: Double =
      Expression.evaluate(Plus(Num(3.7), Num(4.6)))
    val ExpectedEvalDoubleExpressionPlus: Double = 8.3
    val delta: Double = 0.0001
    assertEquals(
      ObtainedEvalDoubleExpressionPlus,
      ExpectedEvalDoubleExpressionPlus,
      delta
    )
  }

  test("Evaluation of Dataset number expression is correct") {
    val ObtainedEvalDfExpressionNum: Dataset[Row] =
      Expression.evaluate(Num(DataFramesExemples.df1))
    val ExpectedEvalDfExpressionNum: Dataset[Row] = DataFramesExemples.df1
    assertEquals(
      ObtainedEvalDfExpressionNum.contentHash,
      ExpectedEvalDfExpressionNum.contentHash
    )
  }

  test("Evaluation of complex Dataset expression is correct") {
    val ObtainedEvalDfExpressionMix: Dataset[Row] =
      Expression
        .evaluate(
          Mult(
            Plus(Num(DataFramesExemples.df1), Num(DataFramesExemples.df1)),
            Num(DataFramesExemples.df1)
          )
        )
        .sort("index")
    ObtainedEvalDfExpressionMix.show()
    val ExpectedEvalDfExpressionMix: Dataset[Row] =
      DataFramesExemples.df2.sort("index")
    ExpectedEvalDfExpressionMix.show()
    assertEquals(
      ObtainedEvalDfExpressionMix.contentHash,
      ExpectedEvalDfExpressionMix.contentHash
    )
  }

  test("Evaluation of Dataset addition expression is correct") {
    val ObtainedEvalDfExpressionPlus: Dataset[Row] =
      Expression.evaluate(
        Plus(Num(DataFramesExemples.df1), Num(DataFramesExemples.df1))
      )
    val ExpectedEvalDfExpressionPlus: Dataset[Row] = DataFramesExemples.df3
    assertEquals(
      ObtainedEvalDfExpressionPlus.contentHash,
      ExpectedEvalDfExpressionPlus.contentHash
    )
  }

end ArithmeticOperationSuite
