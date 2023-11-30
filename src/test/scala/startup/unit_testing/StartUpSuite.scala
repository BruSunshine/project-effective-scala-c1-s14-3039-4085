package startup.unit_testing

import munit.FunSuite

import startup.ast.myStart
class StartUpSuite extends munit.FunSuite:

  test("add result in correct addition") {
    val obtained: Int = myStart(0).add(1, 1)
    val expected: Int = 2
    assertEquals(obtained, expected)
  }

end StartUpSuite
