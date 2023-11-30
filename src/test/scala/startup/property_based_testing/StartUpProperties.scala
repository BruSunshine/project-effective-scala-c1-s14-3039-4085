package startup.property_based_testing

import startup.ast.myStart

import org.scalacheck.Prop.forAll
import org.scalacheck.Gen

class StartUpPropertiesSuite extends munit.ScalaCheckSuite:
  
  val addDomain: (Gen[Int], Gen[Int]) = (Gen.choose(-10, 50), Gen.choose(-10, 50))

  property("all add numbers are positiv") {
    forAll(addDomain(0).suchThat(x => x > 0), addDomain(1).suchThat(x => x > 0)) { (a: Int, b: Int) => myStart(0).add(a, b) >= 0 }
  }
