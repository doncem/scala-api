name := "api"

version := "0.1"

scalaVersion := "2.13.4"

lazy val root = (project in file("."))
  .configs(IntegrationTest)
  .settings(
    Defaults.itSettings,
    libraryDependencies ++= Seq(
      "ch.qos.logback" % "logback-classic" % "1.3.0-alpha5",
      "com.github.pureconfig" %% "pureconfig" % "0.14.0",
      "com.github.pureconfig" %% "pureconfig-fs2" % "0.14.0",
      "com.github.pureconfig" %% "pureconfig-yaml" % "0.14.0",
      "com.typesafe.scala-logging" %% "scala-logging"   % "3.9.2",
      "dev.profunktor" %% "http4s-tracer" % "1.5.3",
      "io.circe" %% "circe-generic" % "0.13.0",
      "org.http4s" %% "http4s-blaze-client" % "0.21.13" % "it",
      "org.http4s" %% "http4s-blaze-server" % "0.21.13",
      "org.http4s" %% "http4s-circe" % "0.21.13",
      "org.http4s" %% "http4s-dsl" % "0.21.13",
      "org.scalatest" %% "scalatest" % "3.2.2" % "test,it",
      "org.typelevel" %% "cats-effect" % "2.3.0"
    )
  )
