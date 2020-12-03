package lt.donatasmart.api

import cats.effect.Sync
import dev.profunktor.tracer.{Http4sTracerDsl, Tracer}
import dev.profunktor.tracer.TracedHttpRoute
import lt.donatasmart.api.model.response.Message
import io.circe.generic.auto.exportEncoder
import org.http4s.{HttpApp, HttpRoutes}
import org.http4s.circe.CirceEntityEncoder.circeEntityEncoder
import org.http4s.implicits.http4sKleisliResponseSyntaxOptionT
import org.http4s.server.Router

class Api[F[_]: Sync: Tracer](name: String) extends Http4sTracerDsl[F] {

  private val simpleRoutes: HttpRoutes[F] = TracedHttpRoute[F] {
    case GET -> Root using _ => Ok(Message(s"Welcome to $name"))
  }

  val routes: HttpApp[F] = Router(
    "/" -> simpleRoutes
  ).orNotFound
}
