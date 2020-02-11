package lt.donatasmart.api

import java.util.concurrent.{ExecutorService, Executors}

import cats.effect.{Blocker, ContextShift, Fiber, IO, Resource, Timer}
import io.circe.generic.auto.exportDecoder
import lt.donatasmart.api.config.Config
import lt.donatasmart.api.model.response.Message
import org.http4s.Status
import org.http4s.circe.CirceEntityDecoder.circeEntityDecoder
import org.http4s.client.{Client, JavaNetClientBuilder}
import org.http4s.server.Server
import org.http4s.server.blaze.BlazeServerBuilder
import org.scalatest.BeforeAndAfterAll
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.concurrent.ExecutionContext.global

class ApiTest extends AnyFlatSpec with Matchers with BeforeAndAfterAll {

  implicit val contextShift: ContextShift[IO] = IO.contextShift(global)
  implicit val timer: Timer[IO] = IO.timer(global)

  val api = new Api()
  val server: Resource[IO, Server[IO]] = Config.load.map(config =>
    BlazeServerBuilder[IO].bindLocal(config.http.port).withHttpApp(api.httpApp).resource
  ).unsafeRunSync()
  val fiber: Fiber[IO, Nothing] = server.use(_ => IO.never).start.unsafeRunSync()

  val blockingPool: ExecutorService = Executors.newFixedThreadPool(5)
  val blocker: Blocker = Blocker.liftExecutorService(blockingPool)
  val httpClient: Client[IO] = JavaNetClientBuilder[IO](blocker).create

  "API" should "greet consumer" in {
    httpClient.get[Unit]("http://localhost:4000/") { response =>
      response.status shouldBe Status.Ok
      response.as[Message].unsafeRunSync() shouldEqual Message("Welcome to API")

      IO(null)
    }.unsafeRunSync()
  }

  override protected def afterAll(): Unit = fiber.cancel
}
