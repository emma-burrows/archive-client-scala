package otw.api.response

case class ArchiveApiError(statusCode: Int, error: String) extends ArchiveResponse
case class Error(error: String)
