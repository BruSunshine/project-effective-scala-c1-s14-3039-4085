id: file://<WORKSPACE>/src/main/scala/app/MyApp.scala:[204..210) in Input.VirtualFile("file://<WORKSPACE>/src/main/scala/app/MyApp.scala", "package app

import cask.Main
import web.{MinimalRoutes, StaticFiles, FormPost, JsonPost}
import sparkjobs.Session

trait MyApp

object SparkMain extends MyApp:
  val sparkSession = Session.spark
  def 

object RoutesMain extends cask.Main with MyApp:
  val allRoutes = Seq(MinimalRoutes, StaticFiles, FormPost, JsonPost)
  //override def main(args: Array[String]): Unit = super.main(args)
  //override def host: String = "0.0.0.0"
  //override def verbose: Boolean = true
  //override def port: Int = 8080
  //override def debugMode: Boolean = true
")
file://<WORKSPACE>/src/main/scala/app/MyApp.scala
file://<WORKSPACE>/src/main/scala/app/MyApp.scala:13: error: expected identifier; obtained object
object RoutesMain extends cask.Main with MyApp:
^
#### Short summary: 

expected identifier; obtained object