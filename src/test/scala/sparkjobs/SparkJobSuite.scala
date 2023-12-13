package sparkjobs

import munit.FunSuite
//import org.apache.spark.sql.SparkSession
//import scala.concurrent.duration._
//import scala.concurrent.Await
//import scala.concurrent.Future
//import scala.concurrent.ExecutionContext.Implicits.global

class SparkJobSuite extends munit.FunSuite:

/*
  test("stopSpark should stop a Spark session") {
    val initialSession = org.apache.spark.sql.SparkSession.builder
      .appName("sparkAppTest")
      .master("local[*]")
      .getOrCreate()
    assert(initialSession != null, "SparkSession sparkAppTest is null")
    val future = Future {
      Thread.sleep(10000)
      initialSession.stop()
      Thread.sleep(10000) // 1 second delay
      org.apache.spark.sql.SparkSession.getActiveSession
    }
    val stoppedSession = Await.result(future, 22.seconds)
    assert(
      stoppedSession.isEmpty || stoppedSession.get.sparkContext.appName != "sparkAppTest",
      "SparkSession sparkAppTest is not stopped"
    )
  }
  */

  test("sparkSession can be correctly initialized") {
    val session = Session.spark
    assert(session != null, "SparkSession is null")
    assert(session.sparkContext.appName == "sparkApp", "AppName is incorrect")
    assert(session.sparkContext.master == "local[*]", "Master is incorrect")
  }
