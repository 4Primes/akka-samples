import sbt._
import sbt.Keys._

object AkkaSamplesBuild extends Build {

  lazy val akkaSamples = Project(
    id = "akkaSamples",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "akka-samples",
      organization := "org.fourprimes",
      version := "0.1-SNAPSHOT",
      scalaVersion := "2.10.0",
      resolvers += "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases",
      libraryDependencies ++= Seq(
        "com.typesafe.akka" % "akka-actor" % "2.1.0" cross CrossVersion.binary,
        "com.typesafe.akka" % "akka-remote" % "2.1.0" cross CrossVersion.binary
      )
    )
  )
}
