package lt.donatasmart.api

import cats.effect.{ExitCode, IO, IOApp}
import cats.syntax.functor.toFunctorOps
import fs2.Stream
import lt.donatasmart.api.config.Config
import org.http4s.server.blaze.BlazeServerBuilder

object MainApp extends IOApp {

  lazy val module: Stream[IO, Unit] = for {
    config <- Stream.eval(Config.load)
    api = new Api()
    _ <- BlazeServerBuilder[IO].bindLocal(config.http.port).withHttpApp(api.httpApp).serve
  } yield ()

  override def run(args: List[String]): IO[ExitCode] = module.compile.drain.as(ExitCode.Success)
}
