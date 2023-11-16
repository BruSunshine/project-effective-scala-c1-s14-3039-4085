package startup

import startup.myStart

@main def run(): Unit =
    val myStartinstance = myStart(0)
    val myvalue = myStartinstance.add(2,3)
    println(s"let's start ${myStartinstance.startvalue} with value $myvalue")
