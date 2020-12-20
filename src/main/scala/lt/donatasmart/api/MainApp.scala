package lt.donatasmart.api

import java.util.concurrent.{Executors, TimeUnit}
import cats.effect.{ExitCode, IO, IOApp, Resource}
import dev.profunktor.tracer.Tracer
import dev.profunktor.tracer.instances.tracer.defaultTracer
import dev.profunktor.tracer.instances.tracerlog.defaultLog
import fs2.Stream
import lt.donatasmart.api.core.config
import lt.donatasmart.api.core.config.{AppConfig, Config, WithConfig}
import lt.donatasmart.api.routes.{Route, SimpleRoutes}
import org.http4s.server.blaze.BlazeServerBuilder

import scala.concurrent.ExecutionContext

trait MainApp extends IOApp with WithConfig {

  def allRoutes(config: Config): Seq[Route[IO]] = Seq(new SimpleRoutes(config.app.context.asInstanceOf[AppConfig]))

  def errorHandler: BaseErrorHandler[IO] = BaseErrorHandler[IO]()

  lazy val module: Stream[IO, Unit] = for {
    defaultBanner <- config.defaultBanner
    banner <- config.customBanner
    config <- Stream.eval(configIo)
    ec <- Stream.resource(Resource.make(IO(Executors.newFixedThreadPool(config.app.pool)))(pool => IO {
      pool.shutdown()
      pool.awaitTermination(10, TimeUnit.SECONDS)
      null
    })).map(ExecutionContext.fromExecutorService)
    tracedApi = Tracer[IO].middleware(
      new Api[IO](allRoutes(config)).routes,
      logRequest = config.http.log.request,
      logResponse = config.http.log.response
    )
    _ <- BlazeServerBuilder[IO](ec)
      .bindLocal(config.http.port)
      .withBanner(if (banner.isEmpty) defaultBanner else banner)
      .withHttpApp(tracedApi)
      .withServiceErrorHandler(errorHandler.get)
      .serve
  } yield ()

  override def run(args: List[String]): IO[ExitCode] = module.compile.drain.as(ExitCode.Success)
}
