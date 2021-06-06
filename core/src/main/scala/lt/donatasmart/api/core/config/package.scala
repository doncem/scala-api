package lt.donatasmart.api.core

import java.nio.file.Path
import cats.effect.IO
import fs2.Stream
import fs2.io.file.Files
import fs2.text
import pureconfig.ConfigReader
import pureconfig.generic.auto._
import pureconfig.module.fs2.streamConfig

import scala.io.Source
import scala.util.Try

package object config {

  private val chunkSize = 4096

  private lazy val configStream = Files[IO].readAll(Path.of(getClass.getClassLoader.getResource("application.yaml").getPath), chunkSize)

  def load(implicit appContextReader: ConfigReader[AppContext]): IO[Config] = streamConfig[IO, Config](configStream)
  lazy val defaultBanner: Stream[IO, Seq[String]] = Files[IO]
    .readAll(Path.of(getClass.getClassLoader.getResource("default-banner.txt").getPath), chunkSize)
    .through(text.utf8Decode)
    .through(text.lines)
    .fold(Seq.empty[String])((seq, string) => seq :+ string)
  lazy val customBanner: Stream[IO, Seq[String]] = Stream.eval(IO(
    Try(Source.fromResource("banner.txt"))
      .map(_.getLines())
      .getOrElse(Iterator.empty)
  )).map(_.toSeq)
}
