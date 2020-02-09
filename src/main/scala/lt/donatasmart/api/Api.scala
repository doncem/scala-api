package lt.donatasmart.api

import cats.effect.IO
import org.http4s.{HttpApp, HttpRoutes}
import org.http4s.dsl.io.{->, GET, Ok, Root, http4sOkSyntax}
import org.http4s.implicits.http4sKleisliResponseSyntaxOptionT

class Api {

  lazy val httpApp: HttpApp[IO] = HttpRoutes.of[IO] {
    case GET -> Root => Ok("Welcome to API")
  }.orNotFound
}
