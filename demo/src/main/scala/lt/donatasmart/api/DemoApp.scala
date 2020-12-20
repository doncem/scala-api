package lt.donatasmart.api

import lt.donatasmart.api.core.config.{AppConfig, AppContext}
import pureconfig.ConfigReader
import pureconfig.generic.auto._

object DemoApp extends MainApp {

  implicit lazy val appContextReader: ConfigReader[AppContext] = ConfigReader[AppConfig].map(_.asInstanceOf[AppContext])
}
