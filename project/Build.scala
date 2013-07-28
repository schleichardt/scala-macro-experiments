import sbt._
import sbt.Keys._

object Build extends Build {

  lazy val rootProject = Project(id = "scala-macro-experiments", base = file("."), settings = Project.defaultSettings ++ Seq(
      name := "scala-macro-experiments"
      , organization := "info.schleichardt"
      , version := "0.1-SNAPSHOT"
      , scalaVersion := "2.10.1"
    ) ++ seq(com.github.retronym.SbtOneJar.oneJarSettings: _*)
  )
}
