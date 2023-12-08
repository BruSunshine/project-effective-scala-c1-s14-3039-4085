package startup.unit_testing

import munit.FunSuite
import startup.ast.ArithmeticOperation
import startup.ast.{Expression, Num, Plus, Mult}
import org.apache.spark.sql.{Dataset, Row}
import sparkjobs.DataFramesExemples
import startup.ast.DataFrameName.contentHash

class ExpressionSuite extends munit.FunSuite:

  test("Evaluation of integer number expression is correct") {
    val ObtainedEvalIntExpressionNum: Int = Expression.evaluate(Num(3))
    val ExpectedEvalIntExpressionNum: Int = 3
    assertEquals(ObtainedEvalIntExpressionNum, ExpectedEvalIntExpressionNum)
  }

end ExpressionSuite
