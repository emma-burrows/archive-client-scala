package otw.api.utils

import dispatch._, Defaults._
import scala.collection.JavaConverters._

// Abstraction on top of Dispatch in case the latter gets replaced in future
private[api] class ArchiveHttp(archiveToken: String, archiveApiHost: String) {

  def get(urlPath: String) = {
    val request =
      url(s"http://$archiveApiHost/$urlPath")
        .setContentType("application/json", "utf-8")
        .setHeader("Authorization", s"Token token=$archiveToken")
        .GET

    Http.configure(_.setFollowRedirect(true)){
      request > (x => HttpStatusWithJsonBody(x.getStatusCode, as.json4s.Json(x)))
    }.either
  }

  def post(urlPath: String, jsonBody: String): Future[Either[Throwable, HttpStatusWithJsonBody]] = {
    val request =
      url(s"http://$archiveApiHost/$urlPath")
        .setContentType("application/json", "utf-8")
        .setHeader("Authorization", s"Token token=$archiveToken")
        .setBody(jsonBody)
        .POST

    Http.configure(_.setFollowRedirect(true)) {
      request > (x => HttpStatusWithJsonBody(x.getStatusCode, as.json4s.Json(x)))
    }.either
  }

}
