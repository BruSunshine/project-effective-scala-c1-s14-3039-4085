val scala3Version = "3.3.1"
scalaVersion := scala3Version

name := "myScala3Project"
version := "0.1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  "org.scalameta" %% "munit-scalacheck" % "0.7.29" % Test,
  "com.lihaoyi" %% "cask" % "0.9.1",
  "com.lihaoyi" %% "requests" % "0.8.0",
  "com.lihaoyi" %% "upickle" % "3.1.3",
  "com.lihaoyi" %% "os-lib" % "0.9.2",
  ("org.apache.spark" %% "spark-core" % "3.5.0" % "provided").cross(CrossVersion.for3Use2_13),
  ("org.apache.spark" %% "spark-sql" % "3.5.0" % "provided").cross(CrossVersion.for3Use2_13)
)

//"org.scalameta" %% "munit" % "0.7.26" % Test,
//"org.scalacheck" %% "scalacheck" % "1.15.4" % Test

//scalacOptions ++= Seq(
//  "-coverage-out", "coverage", // destination for measurement files
//  "-sourceroot", "startup" // source root path
//)

unmanagedSourceDirectories in Compile += baseDirectory.value / "worksheets"