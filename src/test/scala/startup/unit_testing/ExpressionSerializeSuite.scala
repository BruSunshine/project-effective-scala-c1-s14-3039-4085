package startup.unit_testing

import munit.FunSuite
import startup.ast.ArithmeticOperation
import startup.ast.{ExpressionToSerialize, Expression, Num}
import org.apache.spark.sql.{Dataset, Row}
import sparkjobs.DataFramesExemples
import startup.ast.DataFrameName.contentHash
import upickle.default.{read, write}
import startup.ast.ExpressionToSerialize.given
import startup.ast.DataFrameName.given
import startup.ast.ArithmeticOperation.given
import startup.ast.DataFrameName.{toDataFrameName}

class ExpressionSerializeSuite extends munit.FunSuite:

  test(
    "ReadWriter[ExpressionToSerialize[Int]] should correctly serialize and deserialize ExpressionToSerialize[Int]"
  ) {
    val expr: ExpressionToSerialize[Int] = ExpressionToSerialize(Right(Num(3)))
    val jsonExpr = write[ExpressionToSerialize[Int]](expr)
    val readExpr = read[ExpressionToSerialize[Int]](jsonExpr)
    assert(expr == readExpr)
  }

  test(
    "ReadWriter[ExpressionToSerialize[Double]] should correctly serialize and deserialize ExpressionToSerialize[Double]"
  ) {
    val expr: ExpressionToSerialize[Double] =
      ExpressionToSerialize(Right(Num(3.8)))
    val jsonExpr = write[ExpressionToSerialize[Double]](expr)
    val readExpr = read[ExpressionToSerialize[Double]](jsonExpr)
    assert(expr == readExpr)
  }

  test(
    "ReadWriter[ExpressionToSerialize[Dataset[Row]]] should correctly serialize and deserialize ExpressionToSerialize[Dataset[Row]]"
  ) {
    val df: Dataset[Row] = DataFramesExemples.df3
    val dfName = df.toDataFrameName
    val expr: ExpressionToSerialize[Dataset[Row]] =
      ExpressionToSerialize(Expression.validateExpression(Num(df)))    
    val jsonExpr: String = write[ExpressionToSerialize[Dataset[Row]]](expr)
    val readExpr: ExpressionToSerialize[Dataset[Row]] =
      read[ExpressionToSerialize[Dataset[Row]]](jsonExpr)
    for
      x <- expr.argast
      y <- readExpr.argast
    yield
      val xeval = Expression.evaluate(x).contentHash
      val yeval = Expression.evaluate(y).contentHash
      assert(xeval == yeval)
  }
