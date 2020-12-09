package lt.donatasmart.api

import java.util.concurrent.{Executors, TimeUnit}

import cats.effect.{ExitCode, IO, IOApp, Resource}
import dev.profunktor.tracer.Tracer
import dev.profunktor.tracer.instances.tracer.defaultTracer
import dev.profunktor.tracer.instances.tracerlog.defaultLog
import fs2.Stream
import lt.donatasmart.api.config.{AppConfig, AppContext, Config}
import lt.donatasmart.api.routes.{Route, SimpleRoutes}
import org.http4s.server.blaze.BlazeServerBuilder
import pureconfig.ConfigReader
import pureconfig.generic.auto._

import scala.concurrent.ExecutionContext

trait MainApp extends IOApp {

  implicit val appContextReader: ConfigReader[AppContext] = ConfigReader[AppConfig].map(_.asInstanceOf[AppContext])

  def allRoutes(config: AppContext): Seq[Route[IO]] = Seq(new SimpleRoutes(config.asInstanceOf[AppConfig]))

  def errorHandler: BaseErrorHandler[IO] = BaseErrorHandler[IO]()

  lazy val module: Stream[IO, Unit] = for {
    defaultBanner <- Config.defaultBanner
    banner <- Config.customBanner
    config <- Stream.eval(Config.load)
    ec <- Stream.resource(Resource.make(IO(Executors.newFixedThreadPool(config.app.pool)))(pool => IO {
      pool.shutdown()
      pool.awaitTermination(10, TimeUnit.SECONDS)
      null
    })).map(ExecutionContext.fromExecutorService)
    tracedApi = Tracer[IO].middleware(
      new Api[IO](allRoutes(config.app.context)).routes,
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
