name := "scala-api"

version := "0.1"

scalaVersion := "2.13.1"

libraryDependencies ++= Seq(
  "com.github.pureconfig" %% "pureconfig" % "0.12.2",
  "com.github.pureconfig" %% "pureconfig-fs2" % "0.12.2",
  "com.github.pureconfig" %% "pureconfig-yaml" % "0.12.2",
  "org.http4s" %% "http4s-blaze-server" % "0.21.0-RC5",
  "org.http4s" %% "http4s-dsl" % "0.21.0-RC5",
  "org.typelevel" %% "cats-effect" % "2.1.1"
)
