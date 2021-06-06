package lt.donatasmart.api

//import cats.effect.{IO, Resource}
import lt.donatasmart.api.core.config.WithConfig

trait WithDb[F[_]] extends WithConfig {

//  type DbResource[G[_]] = Resource[G, F[G]]
//
//  def dbConfig: DbResource[IO]
}
