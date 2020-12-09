package lt.donatasmart.api.routes

import dev.profunktor.tracer.Http4sTracerDsl
import dev.profunktor.tracer.TracedHttpRoute.TracedRequest
import org.http4s.Response

trait Route[F[_]] extends Http4sTracerDsl[F] {

  type Routes = PartialFunction[TracedRequest[F], F[Response[F]]]

  def prefix: String

  def routes: Routes
}
