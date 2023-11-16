package startup.unit_testing

import munit.FunSuite

class ComputationSuite extends munit.FunSuite:

    test("testing main should return the main result"){
        val obtained: String = startup.runComputation()
        assert(obtained.isInstanceOf[String])
    }
end ComputationSuite