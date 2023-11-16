package startup.unit_testing

import munit.FunSuite

class StartUpSuite extends munit.FunSuite:

    test("add result in correct addition"){
        val obtained: Int = startup.myStart(0).add(1,1)
        val expected: Int = 2
        assertEquals(obtained, expected)
    }

    test("testing main should return the main result"){
        val obtained: Unit = startup.run()
        val expected: Unit = None
        assertEquals(obtained, expected)
    }
end StartUpSuite