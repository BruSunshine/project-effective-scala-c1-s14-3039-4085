package startup.unit_testing

import munit.FunSuite
import startup.ast.ArithmeticOperation
import startup.ast.{ExpressionToSerialize, Expression, Num, Plus, Mult}
import org.apache.spark.sql.{Dataset, Row}
import sparkjobs.DataFramesExemples
import startup.ast.DataFrameName.contentHash
import upickle.default.{read, write}
import startup.ast.ExpressionToSerialize.given
import startup.ast.Expression.given
import startup.ast.DataFrameName.given
import startup.ast.ArithmeticOperation.given


class ExpressionSerializeSuite extends munit.FunSuite:

  test("ReadWriter[ExpressionToSerialize[T]] should correctly serialize and deserialize ExpressionToSerialize[T]") {
    val expr = ExpressionToSerialize(Right(Num(3)))
    val json = write(expr)
    val readExpr = read[ExpressionToSerialize[String]](json)
    assert(expr == readExpr)
  }
/*
  test("ReadWriter[Either[String, ExpressionToSerialize[T]]] should correctly serialize and deserialize Either[String, ExpressionToSerialize[T]]") {
    // Create an Either[String, ExpressionToSerialize[T]] instance
    val either = Left("test")

    // Serialize the Either instance to JSON
    val json = write(either)

    // Deserialize the JSON string back to an Either instance
    val readEither = read[Either[String, ExpressionToSerialize[String]]](json)

    // Check that the deserialized Either instance is the same as the original
    assert(either == readEither)
  }
end ExpressionSerializeSuite
*/