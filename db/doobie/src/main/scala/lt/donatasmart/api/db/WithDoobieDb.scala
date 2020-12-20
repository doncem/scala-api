package lt.donatasmart.api.db

import cats.effect.{Blocker, IO, Resource}
import doobie.ExecutionContexts
import doobie.hikari.HikariTransactor
import lt.donatasmart.api.WithDb

trait WithDoobieDb extends WithDb[HikariTransactor] {

  import lt.donatasmart.api.core.config.contextShiftForConfigs

  override lazy val dbConfig: DbResource[IO] = for {
    config <- Resource.make(dbConfigIo)(_ => IO.unit)
    ec <- ExecutionContexts.cachedThreadPool[IO]
    tr <- HikariTransactor.newHikariTransactor[IO](
      config.driverName,
      config.url,
      config.username,
      config.password,
      ec,
      Blocker.liftExecutionContext(ec)
    )
  } yield tr
}
