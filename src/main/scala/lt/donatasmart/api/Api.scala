package lt.donatasmart.api

import cats.effect.Sync
import lt.donatasmart.api.routes.Route
import org.http4s.{HttpApp, HttpRoutes}
import org.http4s.implicits.http4sKleisliResponseSyntaxOptionT
import org.http4s.server.Router

class Api[F[_]: Sync](allRouters: Seq[Route[F]]) {

  private val allRoutes: Seq[(String, HttpRoutes[F])] = allRouters.map(router => router.prefix -> HttpRoutes.of(router.routes))

  val routes: HttpApp[F] = Router(allRoutes:_*).orNotFound
}
