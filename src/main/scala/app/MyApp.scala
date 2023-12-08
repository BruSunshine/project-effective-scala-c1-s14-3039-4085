package app

import cask.Main
import web.{MinimalRoutes, StaticFiles, FormPost, JsonPost}
import sparkjobs.Session

trait MyApp

object SparkMain extends MyApp:
  val sparkSession = Session.spark
  def stopSpark():Unit =
    sparkSession.stop()

object RoutesMain extends cask.Main with MyApp:
  val allRoutes = Seq(MinimalRoutes, StaticFiles, FormPost, JsonPost)
  SparkMain.sparkSession
