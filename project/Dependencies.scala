import sbt._

object Dependencies {

  private val http4sV     = "0.21.13"
  private val pureconfigV = "0.14.0"

  /*
  core ------ api
   */
  lazy val core: Seq[ModuleID] = pureconfig
  lazy val db: Seq[ModuleID] = Seq()
  lazy val api: Seq[ModuleID] = logging ++ http4s ++ json :+
    "org.scalatest" %% "scalatest" % "3.2.2" % "test,it" :+
    "org.typelevel" %% "cats-effect" % "2.3.0" % "test"

  private val pureconfig = Seq(
    "com.github.pureconfig" %% "pureconfig"       % pureconfigV,
    "com.github.pureconfig" %% "pureconfig-fs2"   % pureconfigV,
    "com.github.pureconfig" %% "pureconfig-yaml"  % pureconfigV
  )

  private val logging = Seq(
    "ch.qos.logback"              % "logback-classic" % "1.3.0-alpha5",
    "com.typesafe.scala-logging" %% "scala-logging"   % "3.9.2"
  )

  private val http4s = Seq(
    "dev.profunktor"  %% "http4s-tracer"        % "1.5.3",
    "org.http4s"      %% "http4s-blaze-client"  % http4sV % "it",
    "org.http4s"      %% "http4s-blaze-server"  % http4sV,
    "org.http4s"      %% "http4s-circe"         % http4sV,
    "org.http4s"      %% "http4s-dsl"           % http4sV
  )

  private val json = Seq(
    "io.circe" %% "circe-generic" % "0.13.0"
  )
}
