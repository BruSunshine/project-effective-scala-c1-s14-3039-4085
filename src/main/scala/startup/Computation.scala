package startup

import startup.myStart

def runComputation(): String =
    val myStartinstance = myStart(0)
    val myvalue = myStartinstance.add(2,3)
    s"let's start ${myStartinstance.startvalue} with value $myvalue"