import sbt.Keys._

lazy val commonSettings = Seq(
  version := "0.0.1",
  scalaVersion := "2.13.4",
  scalacOptions ++= Seq(
    "-deprecation",
    "-feature"
  )
)

lazy val core = (project in file("core"))
  .settings(commonSettings: _*)
  .settings(
    name := "api-core",
    libraryDependencies ++= Dependencies.core
  )

lazy val api = (project in file("."))
  .configs(IntegrationTest)
  .settings(commonSettings: _*)
  .settings(
    name := "api",
    Defaults.itSettings,
    libraryDependencies ++= Dependencies.api
  )
  .aggregate(core)
