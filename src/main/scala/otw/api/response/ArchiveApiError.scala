package otw.api.response

case class ArchiveApiError(statusCode: Int, messages: List[String]) extends ArchiveResponse
case class Error(error: String)
