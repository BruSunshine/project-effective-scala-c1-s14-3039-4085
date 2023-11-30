package integration

import cask.Main
import app.MinimalRoutesMain
import startup.dataframe.SparkJob

object MyApp extends cask.Main:

  val allRoutes = MinimalRoutesMain.allRoutes

  val sparkJobRun = SparkJob.run()

  sparkJobRun.onComplete(_ => println("Spark job finished"))