package lt.donatasmart.api.core

trait Error extends Throwable {

  def message: String

  override def getMessage: String = message
}
