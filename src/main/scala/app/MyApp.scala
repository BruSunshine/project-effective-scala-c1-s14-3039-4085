package app

// Importing necessary modules
import cask.Main
import web.{MinimalRoutes, StaticFiles, JsonPost}
import sparkjobs.Session

/*
This code is part of a Scala application that uses the Cask framework for handling HTTP requests and Apache Spark for data processing.
The MyApp trait is a common interface for the application.
The SparkMain object manages the Spark session, and the RoutesMain object manages the routes of the application.
When the application is shutting down, a shutdown hook is added to stop the Spark session.
 */

// MyApp is a trait that serves as a common interface for the application
trait MyApp

// SparkMain is an object that extends MyApp. It is responsible for managing the Spark session.
object SparkMain extends MyApp:
  // sparkSession is a val that holds the Spark session from the Session object in the sparkjobs package.
  val sparkSession = Session.spark
  // stopSpark is a method that stops the Spark session.
  def stopSpark(): Unit =
    sparkSession.stop()

// RoutesMain is an object that extends cask.Main and MyApp. It is responsible for managing the routes of the application.
object RoutesMain extends cask.Main with MyApp:
  // allRoutes is a sequence that holds all the routes for the application.
  val allRoutes = Seq(MinimalRoutes, StaticFiles, JsonPost)
  // Initializing the Spark session
  SparkMain.sparkSession
  // Adding a shutdown hook to stop the Spark session when the application is shutting down.
  sys.addShutdownHook {
    println("Shutting down Spark session...")
    SparkMain.stopSpark()
  }
