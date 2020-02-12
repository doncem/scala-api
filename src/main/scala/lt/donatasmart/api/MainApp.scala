package lt.donatasmart.api

import cats.effect.{ExitCode, IO, IOApp}
import cats.syntax.functor.toFunctorOps
import dev.profunktor.tracer.Tracer
import dev.profunktor.tracer.instances.tracer.defaultTracer
import dev.profunktor.tracer.instances.tracerlog.defaultLog
import fs2.Stream
import lt.donatasmart.api.config.Config
import org.http4s.server.blaze.BlazeServerBuilder

object MainApp extends IOApp {

  lazy val module: Stream[IO, Unit] = for {
    config <- Stream.eval(Config.load)
    api = new Api[IO]()
    tracedApi = Tracer[IO].middleware(api.routes, logRequest = true, logResponse = true)
    _ <- BlazeServerBuilder[IO].bindLocal(config.http.port).withHttpApp(tracedApi).serve
  } yield ()

  override def run(args: List[String]): IO[ExitCode] = module.compile.drain.as(ExitCode.Success)
}
