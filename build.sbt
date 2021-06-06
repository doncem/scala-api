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

lazy val db = (project in file("db"))
  .settings(commonSettings: _*)
  .settings(name := "api-db")
  .dependsOn(core % "test->test;compile->compile")
  .aggregate(core)

lazy val doobieDb = (project in file("db/doobie"))
  .settings(commonSettings: _*)
  .settings(
    name := "api-doobie-db",
    libraryDependencies ++= Dependencies.doobieDb
  )
  .dependsOn(db % "test->test;compile->compile")
  .aggregate(db)

lazy val api = (project in file("."))
  .configs(IntegrationTest)
  .settings(commonSettings: _*)
  .settings(
    name := "api",
    Defaults.itSettings,
    libraryDependencies ++= Dependencies.api
  )
  .dependsOn(core % "test->test;compile->compile")
  .aggregate(core)

lazy val demo = (project in file("demo"))
  .settings(commonSettings: _*)
  .settings(Revolver.settings: _*)
  .settings(name := "api-demo")
  .dependsOn(api % "compile->compile")
  .dependsOn(doobieDb % "compile->compile")
