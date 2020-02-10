package lt.donatasmart.api

import cats.effect.IO
import lt.donatasmart.api.model.response.Message
import io.circe.generic.auto.exportEncoder
import org.http4s.{HttpApp, HttpRoutes}
import org.http4s.circe.CirceEntityEncoder.circeEntityEncoder
import org.http4s.dsl.io.{->, GET, Ok, Root, http4sOkSyntax}
import org.http4s.implicits.http4sKleisliResponseSyntaxOptionT

class Api {

  lazy val httpApp: HttpApp[IO] = HttpRoutes.of[IO] {
    case GET -> Root => Ok(Message("Welcome to API"))
  }.orNotFound
}
