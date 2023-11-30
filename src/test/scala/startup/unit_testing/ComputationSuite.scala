package startup.unit_testing

import munit.FunSuite

import startup.computation.{runComputation}

class ComputationSuite extends munit.FunSuite:

    test("testing main should return the main result"){
        val obtained: String = runComputation()
        assert(obtained.isInstanceOf[String])
    }
end ComputationSuite