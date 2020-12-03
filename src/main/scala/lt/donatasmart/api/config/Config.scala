package lt.donatasmart.api.config

import java.nio.file.Path
import java.util.concurrent.Executors

import cats.effect.{Blocker, ContextShift, IO}
import fs2.io.file
import pureconfig.generic.auto._
import pureconfig.module.fs2.streamConfig

import scala.concurrent.ExecutionContext.Implicits.global

case class App(name: String, pool: Int)
case class Http(host: String, port: Int)

case class Config(app: App, http: Http)

object Config {
  private val chunkSize = 4096

  implicit private val contextShift: ContextShift[IO] = IO.contextShift(global)
  private val blocker: Blocker = Blocker.liftExecutorService(Executors.newCachedThreadPool())

  private lazy val configStream = file.readAll[IO](Path.of(getClass.getClassLoader.getResource("application.yaml").getPath), blocker, chunkSize)

  lazy val load: IO[Config] = streamConfig[IO, Config](configStream)
}
