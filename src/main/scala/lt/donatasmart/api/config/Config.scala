package lt.donatasmart.api.config

import java.nio.file.Path
import java.util.concurrent.Executors

import cats.effect.{Blocker, ContextShift, IO}
import fs2.Stream
import fs2.io.file
import fs2.text
import pureconfig.generic.auto._
import pureconfig.module.fs2.streamConfig

import scala.concurrent.ExecutionContext.Implicits.global
import scala.io.Source
import scala.util.Try

case class App(name: String, pool: Int)
case class Http(host: String, port: Int)

case class Config(app: App, http: Http)

object Config {
  private val chunkSize = 4096

  implicit private val contextShift: ContextShift[IO] = IO.contextShift(global)
  private val blocker: Blocker = Blocker.liftExecutorService(Executors.newCachedThreadPool())

  private lazy val configStream = file.readAll[IO](Path.of(getClass.getClassLoader.getResource("application.yaml").getPath), blocker, chunkSize)

  lazy val load: IO[Config] = streamConfig[IO, Config](configStream)
  lazy val defaultBanner: Stream[IO, Seq[String]] = file
    .readAll[IO](Path.of(getClass.getClassLoader.getResource("default-banner.txt").getPath), blocker, chunkSize)
    .through(text.utf8Decode)
    .through(text.lines)
    .fold(Seq.empty[String])((seq, string) => seq :+ string)
  lazy val customBanner: Stream[IO, Seq[String]] = Stream.eval(IO(
    Try(Source.fromResource("banner.txt"))
      .map(_.getLines())
      .getOrElse(Iterator.empty)
  )).map(_.toSeq)
}
