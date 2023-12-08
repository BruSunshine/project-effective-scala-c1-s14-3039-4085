id: file://<WORKSPACE>/src/test/scala/startup/unit_testing/StartUpSuite.scala:[307..307) in Input.VirtualFile("file://<WORKSPACE>/src/test/scala/startup/unit_testing/StartUpSuite.scala", "package startup.unit_testing

import munit.FunSuite

import startup.ast.myStart
class StartUpSuite extends munit.FunSuite:

  test("add result in correct addition") {
    val obtained: Int = myStart(0).add(1, 1)
    val expected: Int = 2
    assertEquals(obtained, expected)
  }

end StartUpSuite



class 
")
file://<WORKSPACE>/src/test/scala/startup/unit_testing/StartUpSuite.scala
file://<WORKSPACE>/src/test/scala/startup/unit_testing/StartUpSuite.scala:19: error: expected identifier; obtained eof

^
#### Short summary: 

expected identifier; obtained eof