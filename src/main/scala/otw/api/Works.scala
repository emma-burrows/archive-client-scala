package otw.api

import org.json4s.JValue
import otw.api.request.OriginalUrls
import otw.api.response.{ArchiveApiError, Error, WorkFoundResponse, WorkNotFoundResponse}
import otw.api.utils.{ArchiveHttp, Json}

import scala.concurrent.ExecutionContext

private[api] case class Works(archive_token: String,
                              archive_api_url: String) {

  val http = ArchiveHttp(archive_token, archive_api_url)
  val json = new Json

  val checkUrlPath = "api/v1/works/urls"

  def checkUrlsJson(urls: List[String])(implicit ec: ExecutionContext) = {
    val checkFuture = checkUrls(urls)

    checkFuture map {
      case Right(resp) =>
        json.writeJson(resp)

      case Left(ex) =>
        val archiveApiError = ArchiveApiError(500, error = ex.getMessage)
        json.writeJson(archive_api_url)
    }
  }

  def checkUrls(urls: List[String])(implicit ec: ExecutionContext) = {
    val originalUrls = json.writeJson(OriginalUrls(urls))

    val foo = http.post(checkUrlPath, originalUrls)

    foo map {
      case Right(resp)         =>
        resp.status match {
          case x if 200 until 299 contains x =>
            Right(checkResponses(resp.body))

          case 400 =>
            val error = json.readJson[Error](resp.body.children.head)
            Right(ArchiveApiError(resp.status, error.error))
        }
      case Left(ex: Throwable) => println(ex); Left(ex)
    }
  }

  private def checkResponses(body: JValue) = {
    body.children.map { value =>
      val status = json.readJson[Map[String, String]](value).get("status")
      status match {
        case Some("ok")        => json.readJson[WorkFoundResponse](value)
        case Some("not_found") => json.readJson[WorkNotFoundResponse](value)
        case _                 => ArchiveApiError(400, "Cannot parse response into a valid Work object")
      }
    }
  }
}
