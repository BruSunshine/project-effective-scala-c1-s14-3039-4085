package startup

// This file contains ...

/**
 * Interface for the project setup structure
 * 
 * This interface defines the operations for constructing
 * and manipulating ... modeled like this, works like this
 * 
 * @see http://blabla
 */


/** A dummy class to test project setup */
case class myStart(param: Int):

    /** A dummy value */
    val startvalue: String = "test"
    
    /** @param two integers to add */
    /** @return the addition of the 2 inputs */
    def add(x: Int, y: Int): Int = x + y

end myStart

/** A class that simulates a stateful HTTP server */
class HttpServer:
  
  /** Start the HTTP server on the TCP port */
  def start(port: Int): Unit = ()
  
  /** Stop the HTTP server */
  def stop(): Unit = ()

end HttpServer