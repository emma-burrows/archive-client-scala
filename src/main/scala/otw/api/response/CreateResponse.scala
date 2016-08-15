package otw.api.response

case class CreateResponse(
                           messages: List[String],
                           works: List[ItemCreateResponse]
                         )

case class ItemCreateResponse(
                           status: String,
                           url: String,
                           originalId: String,
                           originalUrl: String,
                           messages: List[String]
                         )
