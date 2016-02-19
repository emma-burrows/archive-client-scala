package otw.api.response

case class CreateResponse(
                           status: String,
                           messages: List[String],
                           works: List[ItemCreateResponse]
                         )

case class ItemCreateResponse(
                           status: String,
                           url: String,
                           originalUrl: String,
                           messages: List[String]
                         )
