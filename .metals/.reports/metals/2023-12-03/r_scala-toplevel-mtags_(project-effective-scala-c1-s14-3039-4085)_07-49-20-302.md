id: file://<WORKSPACE>/src/main/scala/app/MyApp.scala:[157..160) in Input.VirtualFile("file://<WORKSPACE>/src/main/scala/app/MyApp.scala", "package app

import cask.Main
import web.{MinimalRoutes, StaticFiles, FormPost, JsonPost}
import sparkjobs.Session

trait MyApp extends cask.Main

object 
  val sparkSession = Session.spark

object RoutesMain extends MyApp:
  val allRoutes = Seq(MinimalRoutes, StaticFiles, FormPost, JsonPost)
  //override def main(args: Array[String]): Unit = super.main(args)
  //override def host: String = "0.0.0.0"
  //override def verbose: Boolean = true
  //override def port: Int = 8080
  //override def debugMode: Boolean = true
")
file://<WORKSPACE>/src/main/scala/app/MyApp.scala
file://<WORKSPACE>/src/main/scala/app/MyApp.scala:10: error: expected identifier; obtained val
  val sparkSession = Session.spark
  ^
#### Short summary: 

expected identifier; obtained val