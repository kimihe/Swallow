name := "swallow"

version := "1.0"

scalaVersion := "2.12.1"

lazy val akkaVersion = "2.5.2"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",

  "com.typesafe.akka" %% "akka-remote" % akkaVersion
)