val scala3Version = "3.3.1"
scalaVersion := scala3Version

name := "myScala3Project"
version := "0.1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  "org.scalameta" %% "munit-scalacheck" % "0.7.29" % Test
)
//"org.scalameta" %% "munit" % "0.7.26" % Test,
//"org.scalacheck" %% "scalacheck" % "1.15.4" % Test

scalacOptions ++= Seq(
  "-coverage-out", "coverage", // destination for measurement files
  "-sourceroot", "startup" // source root path
)
