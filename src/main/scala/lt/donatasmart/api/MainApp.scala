package lt.donatasmart.api

import java.util.concurrent.{Executors, TimeUnit}

import cats.effect.{ExitCode, IO, IOApp, Resource}
import dev.profunktor.tracer.Tracer
import dev.profunktor.tracer.instances.tracer.defaultTracer
import dev.profunktor.tracer.instances.tracerlog.defaultLog
import fs2.Stream
import lt.donatasmart.api.config.Config
import org.http4s.server.blaze.BlazeServerBuilder

import scala.concurrent.ExecutionContext

object MainApp extends IOApp {

  lazy val module: Stream[IO, Unit] = for {
    banner <- Config.banner
    config <- Stream.eval(Config.load)
    ec <- Stream.resource(Resource.make(IO(Executors.newFixedThreadPool(config.app.pool)))(pool => IO {
      pool.shutdown()
      pool.awaitTermination(10, TimeUnit.SECONDS)
      null
    })).map(ExecutionContext.fromExecutorService)
    api = new Api[IO](config.app.name)
    tracedApi = Tracer[IO].middleware(api.routes, logRequest = true, logResponse = true)
    _ <- BlazeServerBuilder[IO](ec).bindLocal(config.http.port).withHttpApp(tracedApi).withBanner(banner).serve
  } yield ()

  override def run(args: List[String]): IO[ExitCode] = module.compile.drain.as(ExitCode.Success)
}
