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
  "org.apache.logging.log4j" % "log4j-core" % "2.20.0",
  ("org.apache.spark" %% "spark-core" % "3.5.0")
    .cross(CrossVersion.for3Use2_13),
  ("org.apache.spark" %% "spark-sql" % "3.5.0").cross(CrossVersion.for3Use2_13)
)

scalacOptions ++= Seq(
  // "-sourceroot", "startup", // source root path
  "-Wunused:imports", // show warnings during compilation
  "-Ylog:typer"
)

Test / scalacOptions ++= Seq(
  "-coverage-out",
  "coverage" // destination for measurement files
)

Compile / unmanagedSourceDirectories += baseDirectory.value / "worksheets"
