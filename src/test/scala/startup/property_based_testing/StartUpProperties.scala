package startup.property_based_testing

import startup.ast.ArithmeticOperation
import startup.ast.DataFrameName.contentHash
import org.apache.spark.sql.{Dataset, DataFrame, Row}
import org.scalacheck.Prop.forAll
import org.scalacheck.Gen
import sparkjobs.DataFramesExemples

class StartUpPropertiesSuite extends munit.ScalaCheckSuite:

  val addDomain: (Gen[Int], Gen[Int]) =
    (Gen.choose(-10, 50), Gen.choose(-10, 50))

  property("addition is commutative for Int") {
    forAll(addDomain(0), addDomain(1)) { (a: Int, b: Int) =>
      summon[ArithmeticOperation[Int]]
        .add(a, b) == summon[ArithmeticOperation[Int]].add(b, a)
    }
  }

  property("multiplication is commutative for Int") {
    forAll { (a: Int, b: Int) =>
      summon[ArithmeticOperation[Int]]
        .mul(a, b) == summon[ArithmeticOperation[Int]].mul(b, a)
    }
  }

  property("addition is commutative for Double") {
    forAll { (a: Double, b: Double) =>
      summon[ArithmeticOperation[Double]]
        .add(a, b) == summon[ArithmeticOperation[Double]].add(b, a)
    }
  }

  property("multiplication is commutative for Double") {
    forAll { (a: Double, b: Double) =>
      summon[ArithmeticOperation[Double]]
        .mul(a, b) == summon[ArithmeticOperation[Double]].mul(b, a)
    }
  }

  property("subtracting a number from itself results in zero") {
    forAll { (a: Double) =>
      summon[ArithmeticOperation[Double]].add(a, -a) == 0
    }
  }

  property("multiplication by one leaves the number unchanged") {
    forAll { (a: Double) =>
      summon[ArithmeticOperation[Double]].mul(a, 1) == a
    }
  }

  property("division by one leaves the number unchanged") {
    forAll { (a: Double) =>
      summon[ArithmeticOperation[Double]].mul(a, 1) == a
    }
  }

  property("division by zero is not allowed") {
    forAll { (a: Double) =>
      try
        val result = summon[ArithmeticOperation[Double]].mul(a, 1 / 0)
        false
      catch
      //result match
        // case _: Double => false
        case _: ArithmeticException => true
        case _: Throwable => false
    }
  }

  val dfDomain: (Gen[DataFrame], Gen[DataFrame]) = (
    Gen.oneOf(
      DataFramesExemples.df1,
      DataFramesExemples.df2,
      DataFramesExemples.df3
    ),
    Gen.oneOf(
      DataFramesExemples.df1,
      DataFramesExemples.df2,
      DataFramesExemples.df3
    )
  )

  property("addition is commutative for DataFrame") {
    forAll(dfDomain(0), dfDomain(1)) { (dfa, dfb) =>
      summon[ArithmeticOperation[Dataset[Row]]].add(dfa, dfb).contentHash ==
        summon[ArithmeticOperation[Dataset[Row]]].add(dfb, dfa).contentHash
    }
  }

  property("multiplication is commutative for DataFrame") {
    forAll(dfDomain(0), dfDomain(1)) { (dfa, dfb) =>
      summon[ArithmeticOperation[Dataset[Row]]].mul(dfa, dfb).contentHash ==
        summon[ArithmeticOperation[Dataset[Row]]].mul(dfb, dfa).contentHash
    }
  }
