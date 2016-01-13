import Keys._

lazy val archiveClient = (project in file(".")).
                settings(
                  organization := "org.otw",
                  name := "archive-client",
                  version := "0.1-SNAPSHOT",
                  scalaVersion := "2.11.7"
                )

libraryDependencies ++= Seq(
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.3",
  "net.databinder.dispatch" %% "dispatch-json4s-native" % "0.11.3",
  "org.json4s" %% "json4s-ext" % "3.3.0",
  "org.specs2" %% "specs2" % "3.7" % Test,
  "com.github.tomakehurst" % "wiremock" % "1.57" % Test
)

fork in Test := true
