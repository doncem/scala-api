package lt.donatasmart.api.routes

import org.http4s.dsl.Http4sDsl
import org.http4s.{Request, Response}

trait Route[F[_]] extends Http4sDsl[F] {

  type Routes = PartialFunction[Request[F], F[Response[F]]]

  def prefix: String

  def routes: Routes
}
