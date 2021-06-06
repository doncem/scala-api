package lt.donatasmart.api.db

import cats.effect.{IO, Resource}
import doobie.ExecutionContexts
import doobie.hikari.HikariTransactor
import lt.donatasmart.api.core.config.WithConfig

trait WithDoobieDb extends WithConfig {

  lazy val dbConfig: Resource[IO, HikariTransactor[IO]] = for {
    config <- Resource.make(dbConfigIo)(_ => IO.unit)
    ec <- ExecutionContexts.cachedThreadPool[IO]
    tr <- HikariTransactor.newHikariTransactor[IO](
      config.driverName,
      config.url,
      config.username,
      config.password,
      ec
    )
  } yield tr
}
