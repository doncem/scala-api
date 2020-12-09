package lt.donatasmart.api

import cats.arrow.FunctionK
import cats.effect.Sync
import com.typesafe.scalalogging.LazyLogging
import io.circe.generic.auto.exportEncoder
import lt.donatasmart.api.model.response.Message
import org.http4s.dsl.impl.{Responses, Statuses}
import org.http4s.server.ServiceErrorHandler
import org.http4s.circe.CirceEntityEncoder.circeEntityEncoder

trait BaseErrorHandler[F[_]] extends Statuses with Responses[F, F] with LazyLogging {

  val liftG: FunctionK[F, F] = FunctionK.id[F]

  def get: ServiceErrorHandler[F]
}

private [api] object BaseErrorHandler {

  def apply[F[_]: Sync](): BaseErrorHandler[F] = new BaseErrorHandler[F] {
    override def get: ServiceErrorHandler[F] = _ => {
      case e: Throwable => logger.error("BADBAD", e); InternalServerError(Message("BAD"))
    }
  }
}
