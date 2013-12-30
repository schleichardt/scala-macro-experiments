
name := "scala-macro-experiments"

scalaVersion := "2.10.3"

scalacOptions ++= Seq("-feature")

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.0" % "test"

libraryDependencies += "com.typesafe.play" %% "play" % "2.2.1"

libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value

libraryDependencies += "org.scala-lang" % "scala-compiler" % scalaVersion.value