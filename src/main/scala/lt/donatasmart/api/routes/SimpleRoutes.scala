package lt.donatasmart.api.routes

import cats.effect.Sync
import io.circe.generic.auto.exportEncoder
import lt.donatasmart.api.core.config.AppConfig
import lt.donatasmart.api.model.response.Message
import org.http4s.circe.CirceEntityEncoder.circeEntityEncoder

class SimpleRoutes[F[_]: Sync](config: AppConfig) extends Route[F] {

  override lazy val prefix: String = "/"

  override lazy val routes: Routes = {
    case GET -> Root => Ok(Message(s"Welcome to ${config.name}"))
  }
}
