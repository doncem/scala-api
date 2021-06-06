package lt.donatasmart.api

import java.util.concurrent.{Executors, TimeUnit}
import cats.effect.{ExitCode, IO, IOApp, Resource}
import fs2.Stream
import lt.donatasmart.api.core.config
import lt.donatasmart.api.core.config.{AppConfig, Config, WithConfig}
import lt.donatasmart.api.routes.{Route, SimpleRoutes}
import org.http4s.blaze.server.BlazeServerBuilder

import scala.concurrent.ExecutionContext

trait MainApp extends IOApp with WithConfig {

  def allRoutes(config: Config): Seq[Route[IO]] = Seq(new SimpleRoutes(config.app.context.asInstanceOf[AppConfig]))

  def errorHandler: BaseErrorHandler[IO] = BaseErrorHandler[IO]()

  def serverBuilder(ec: ExecutionContext, config: Config, banner: Seq[String]): BlazeServerBuilder[IO] = BlazeServerBuilder[IO](ec)
      .bindLocal(config.http.port)
      .withBanner(banner)
      .withHttpApp(new Api[IO](allRoutes(config)).routes)
      .withServiceErrorHandler(errorHandler.get)

  lazy val module: Stream[IO, Unit] = for {
    defaultBanner <- config.defaultBanner
    banner <- config.customBanner
    config <- Stream.eval(configIo)
    ec <- Stream.resource(Resource.make(IO(Executors.newFixedThreadPool(config.app.pool)))(pool => IO {
      pool.shutdown()
      pool.awaitTermination(10, TimeUnit.SECONDS)
      null
    })).map(ExecutionContext.fromExecutorService)
    _ <- serverBuilder(ec, config, if (banner.isEmpty) defaultBanner else banner).serve
  } yield ()

  override def run(args: List[String]): IO[ExitCode] = module.compile.drain.as(ExitCode.Success)
}
