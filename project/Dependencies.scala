import sbt._

object Dependencies {

  private val http4sV     = "0.23.0-RC1"
  private val pureconfigV = "0.15.0"

  /*
  core ------ api
   */
  lazy val core: Seq[ModuleID] = pureconfig :+ "org.typelevel" %% "cats-effect" % "3.1.1"
  lazy val doobieDb: Seq[ModuleID] = Seq(
    "org.tpolecat" %% "doobie-hikari" % "1.0.0-M5"
  )
  lazy val api: Seq[ModuleID] = logging ++ http4s ++ json :+ "org.scalatest" %% "scalatest" % "3.2.9" % "test,it"

  private val pureconfig = Seq(
    "com.github.pureconfig" %% "pureconfig"       % pureconfigV,
    "com.github.pureconfig" %% "pureconfig-fs2"   % pureconfigV,
    "com.github.pureconfig" %% "pureconfig-yaml"  % pureconfigV
  )

  private val logging = Seq(
    "ch.qos.logback"              % "logback-classic" % "1.3.0-alpha5",
    "com.typesafe.scala-logging" %% "scala-logging"   % "3.9.3"
  )

  private val http4s = Seq(
    "org.http4s"  %% "http4s-blaze-client"  % http4sV % "it",
    "org.http4s"  %% "http4s-blaze-server"  % http4sV,
    "org.http4s"  %% "http4s-circe"         % http4sV,
    "org.http4s"  %% "http4s-dsl"           % http4sV
  )

  private val json = Seq(
    "io.circe" %% "circe-generic" % "0.14.1"
  )
}
