package otw.api.response

import org.joda.time.DateTime

case class FindWorkResponse(statusCode: Int,
                           body: List[ArchiveResponse]) extends ArchiveResponse

sealed trait WorkCheckResponse extends ArchiveResponse {
  val status: String
  val originalId: String
  val originalUrl: String
}

case class WorkNotFoundResponse(status: String,
                                originalId: String,
                                originalUrl: String,
                                error: String) extends WorkCheckResponse

case class WorkFoundResponse(status: String,
                             originalId: String,
                             originalUrl: String,
                             workUrl: String,
                             created: DateTime) extends WorkCheckResponse

