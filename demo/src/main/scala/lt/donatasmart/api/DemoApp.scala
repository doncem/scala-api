package lt.donatasmart.api

import cats.effect.IO
import cats.effect.unsafe.IORuntime
import doobie.hikari.HikariTransactor
import fs2.Stream
import lt.donatasmart.api.core.config.{AppConfig, AppContext, Config}
import lt.donatasmart.api.db.WithDoobieDb
import lt.donatasmart.api.routes.Route
import pureconfig.ConfigReader
import pureconfig.generic.auto._

object DemoApp extends MainApp {

  implicit lazy val appContextReader: ConfigReader[AppContext] = ConfigReader[AppConfig].map(_.asInstanceOf[AppContext])
}

object DemoDbApp extends MainApp with WithDoobieDb {

  implicit lazy val appContextReader: ConfigReader[AppContext] = ConfigReader[AppConfig].map(_.asInstanceOf[AppContext])
  implicit lazy val ioGlobal: IORuntime = IORuntime.global

  def allRoutes(config: Config, transactor: HikariTransactor[IO]): Seq[Route[IO]] = super.allRoutes(config)

  override def allRoutes(config: Config): Seq[Route[IO]] = (for {
    tr <- Stream.resource(dbConfig)
  } yield allRoutes(config, tr)).compile.foldMonoid.unsafeRunSync()
}
