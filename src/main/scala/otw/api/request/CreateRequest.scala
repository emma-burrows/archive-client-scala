package otw.api.request

case class CreateRequest(
                          archivist: String,
                          sendClaimEmails: Boolean,
                          postWithoutPreview: Boolean,
                          encoding: String,
                          collectionNames: String,
                          items: List[Item]
                        )
