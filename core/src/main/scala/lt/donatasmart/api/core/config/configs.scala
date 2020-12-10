package lt.donatasmart.api.core.config

trait AppContext

case class AppConfig(name: String) extends AppContext
case class App(context: AppContext, pool: Int)
case class Log(request: Boolean, response: Boolean)
case class Http(host: String, log: Log, port: Int)

case class Config(app: App, http: Http)
