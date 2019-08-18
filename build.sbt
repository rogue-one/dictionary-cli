// The simplest possible sbt build file is just one line:

scalaVersion := "2.12.8"

name := "hello-world"
organization := "org.chlr.rogue1.dictionary"
version := "1.0"


libraryDependencies ++= Seq(
  "org.scalacheck" %% "scalacheck" % "1.14.0" % Test,
  "org.scalatest" %% "scalatest" % "3.0.8" % Test
)
