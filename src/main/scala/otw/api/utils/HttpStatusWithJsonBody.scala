package otw.api.utils

import org.json4s.JsonAST.JValue

case class HttpStatusWithJsonBody(status: Int,
                                  body: JValue)
