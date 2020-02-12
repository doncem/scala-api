name := "scala-api"

version := "0.1"

scalaVersion := "2.13.1"

lazy val root = (project in file("."))
  .configs(IntegrationTest)
  .settings(
    Defaults.itSettings,
    libraryDependencies ++= Seq(
      "ch.qos.logback" % "logback-classic" % "1.3.0-alpha5",
      "com.github.pureconfig" %% "pureconfig" % "0.12.2",
      "com.github.pureconfig" %% "pureconfig-fs2" % "0.12.2",
      "com.github.pureconfig" %% "pureconfig-yaml" % "0.12.2",
      "com.typesafe.scala-logging" %% "scala-logging"   % "3.9.2",
      "dev.profunktor" %% "http4s-tracer" % "1.4.0-RC1",
      "io.circe" %% "circe-generic" % "0.13.0",
      "org.http4s" %% "http4s-blaze-client" % "0.21.0" % "it",
      "org.http4s" %% "http4s-blaze-server" % "0.21.0",
      "org.http4s" %% "http4s-circe" % "0.21.0",
      "org.http4s" %% "http4s-dsl" % "0.21.0",
      "org.scalatest" %% "scalatest" % "3.1.0" % "test,it",
      "org.typelevel" %% "cats-effect" % "2.1.1"
    )
  )
