package lt.donatasmart.api.core.config

import cats.effect.IO
import pureconfig.ConfigReader

trait WithConfig {

  implicit def appContextReader: ConfigReader[AppContext]

  lazy val configIo: IO[Config] = load

  lazy val dbConfigIo: IO[Db] = configIo.flatMap(config =>
    IO.fromOption(config.db)(MissingConfigError("Missing DB configuration"))
  )
}
