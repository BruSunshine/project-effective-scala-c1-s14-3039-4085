package app

import cask.Main
import web.{MinimalRoutes, StaticFiles, JsonPost}
import sparkjobs.Session

trait MyApp

object SparkMain extends MyApp:
  val sparkSession = Session.spark
  def stopSpark(): Unit =
    sparkSession.stop()

object RoutesMain extends cask.Main with MyApp:
  val allRoutes = Seq(MinimalRoutes, StaticFiles, JsonPost)
  SparkMain.sparkSession

  sys.addShutdownHook {
    println("Shutting down Spark session...")
    SparkMain.stopSpark()
  }
