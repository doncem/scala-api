package lt.donatasmart.api

import cats.effect.unsafe.IORuntime

import java.util.concurrent.{ExecutorService, Executors}
import cats.effect.{FiberIO, IO, Resource}
import io.circe.generic.auto.exportDecoder
import lt.donatasmart.api.core.config
import lt.donatasmart.api.core.config.{AppConfig, AppContext}
import lt.donatasmart.api.model.response.Message
import org.http4s.Status
import org.http4s.circe.CirceEntityDecoder.circeEntityDecoder
import org.http4s.client.{Client, JavaNetClientBuilder}
import org.http4s.server.Server
import org.scalatest.BeforeAndAfterAll
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import pureconfig.ConfigReader
import pureconfig.generic.auto._

import scala.concurrent.ExecutionContext.global

class ApiTest extends AnyFlatSpec with Matchers with BeforeAndAfterAll {
  object TestApp extends MainApp {
    implicit lazy val appContextReader: ConfigReader[AppContext] = ConfigReader[AppConfig].map(_.asInstanceOf[AppContext])
  }

  implicit lazy val ioGlobal: IORuntime = IORuntime.global

  private val API_NAME = "IT Tests"

  val server: Resource[IO, Server] =  config.load(TestApp.appContextReader).map(appConfig =>
    TestApp.serverBuilder(
      global,
      appConfig,
      Seq.empty
    ).resource
  ).unsafeRunSync()
  val fiber: FiberIO[Nothing] = server.use(_ => IO.never).start.unsafeRunSync()

  val blockingPool: ExecutorService = Executors.newFixedThreadPool(5)
  val httpClient: Client[IO] = JavaNetClientBuilder[IO].create

  "API" should "greet consumer" in {
    httpClient.get[Unit]("http://localhost:5000/") { response =>
      response.status shouldBe Status.Ok
      response.as[Message].unsafeRunSync() shouldEqual Message(s"Welcome to $API_NAME")

      IO(null)
    }.unsafeRunSync()
  }

  override protected def afterAll(): Unit = fiber.cancel
}
