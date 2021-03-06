package lt.donatasmart.api.core.config

trait AppContext

case class AppConfig(name: String) extends AppContext
case class App(context: AppContext, pool: Int)
case class Db(driverName: String, url: String, username: String, password: String)
case class Http(host: String, port: Int)

case class Config(app: App, db: Option[Db], http: Http)
