package otw.api.response

import org.joda.time.DateTime
import otw.api.request.OriginalUrls

case class CheckResponse(statusCode: Int,
                         body: List[WorkCheckResponse])

sealed trait WorkCheckResponse {
  val status: String
  val originalUrl: String
}

//(status: Option[String],
//originalUrl: Option[String],
//                            error: Option[String],
//workUrl: Option[String],
//created: Option[DateTime])

case class WorkNotFoundResponse(status: String,
                                originalUrl: String,
                                error: String) extends WorkCheckResponse

case class WorkFoundResponse(status: String,
                             originalUrl: String,
                             workUrl: String,
                             created: DateTime) extends WorkCheckResponse

