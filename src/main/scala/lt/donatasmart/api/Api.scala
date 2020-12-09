package lt.donatasmart.api

import cats.effect.Sync
import dev.profunktor.tracer.{Http4sTracerDsl, Tracer}
import dev.profunktor.tracer.TracedHttpRoute
import lt.donatasmart.api.routes.Route
import org.http4s.{HttpApp, HttpRoutes}
import org.http4s.implicits.http4sKleisliResponseSyntaxOptionT
import org.http4s.server.Router

class Api[F[_]: Sync: Tracer](allRouters: Seq[Route[F]]) extends Http4sTracerDsl[F] {

  private val allRoutes: Seq[(String, HttpRoutes[F])] = allRouters.map(router => router.prefix -> TracedHttpRoute[F] {
    router.routes
  })

  val routes: HttpApp[F] = Router(allRoutes:_*).orNotFound
}
