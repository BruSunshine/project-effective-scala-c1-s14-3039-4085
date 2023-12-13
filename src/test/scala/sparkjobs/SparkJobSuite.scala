package sparkjobs

import munit.FunSuite
class SparkJobSuite extends munit.FunSuite:

  test("sparkSession can be correctly initialized") {
    val session = Session.spark
    assert(session != null, "SparkSession is null")
    assert(session.sparkContext.appName == "sparkApp", "AppName is incorrect")
    assert(session.sparkContext.master == "local[*]", "Master is incorrect")
  }
