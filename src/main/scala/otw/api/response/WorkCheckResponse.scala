package otw.api.response

import org.joda.time.DateTime

case class FindUrlResponse(statusCode: Int,
                           body: List[ArchiveResponse]) extends ArchiveResponse

sealed trait WorkCheckResponse extends ArchiveResponse {
  val status: String
  val originalUrl: String
}

case class WorkNotFoundResponse(status: String,
                                originalUrl: String,
                                error: String) extends WorkCheckResponse

case class WorkFoundResponse(status: String,
                             originalUrl: String,
                             workUrl: String,
                             created: DateTime) extends WorkCheckResponse

