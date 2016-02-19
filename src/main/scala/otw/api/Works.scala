package otw.api

import org.json4s.JValue
import org.json4s.JsonAST.JNothing
import otw.api.request._
import otw.api.response._
import otw.api.utils.{ArchiveHttp, Json}

import scala.concurrent.{ExecutionContext, Future}

private[api] case class Works(archive_token: String,
                              archive_api_url: String,
                              httpClient: Option[ArchiveHttp] = None) {

  val http = httpClient.getOrElse(new ArchiveHttp(archive_token, archive_api_url))
  val checkUrlPath = "api/v1/works/urls"
  val bookmarkPath = "api/v1/bookmarks/"
  val workPath = "api/v1/import/"

  // Create works
  def createItems(`type`: ItemType, createRequest: CreateRequest)(implicit ec: ExecutionContext) = {

    val requestJson = Json.writeJson(createRequest)

    val create = `type` match {
      case WorkItem     => http.post(workPath, requestJson)

      case BookmarkItem => http.post(bookmarkPath, requestJson)
    }

    create map {
      case Right(resp) =>
        resp.status match {
          case status if 200 until 299 contains status =>
            if (resp.body != JNothing)
              Right(createResponse(resp.body))
            else
              Right(ArchiveApiError(resp.status, "No information returned from remote server"))

          case 400 =>
            val error = Json.readJson[Error](resp.body.children.head)
            Right(ArchiveApiError(resp.status, error.error))
        }
    }
  }

  // Find works on the Archive
  def checkUrls(urls: List[String])(implicit ec: ExecutionContext): Future[Either[Throwable, ArchiveResponse]] = {
    val originalUrls = Json.writeJson(OriginalUrls(urls))

    val check = http.post(checkUrlPath, originalUrls)

    check map {
      case Right(resp) =>
        println("resp in checkUrls: " + resp)
        resp.status match {
          case status if 200 until 299 contains status =>
            if (resp.body != JNothing)
              Right(FindUrlResponse(status, checkResponses(resp.body)))
            else
              Right(ArchiveApiError(resp.status, "No information returned from remote server"))

          case 400 =>
            val error = Json.readJson[Error](resp.body.children.head)
            Right(ArchiveApiError(resp.status, error.error))
        }

      case Left(ex) => println(ex); Left(ex)
    }
  }

  private def createResponse(body: JValue) =
    body.children.map { value =>
      println("createResponse: " + value)
      value
    }

  private def checkResponses(body: JValue): List[ArchiveResponse] = {
    body.children.map { value =>
      println("checkResponses: " + value)
      val status = Json.readJson[Map[String, String]](value).get("status")
      status match {
        case Some("ok")        => Json.readJson[WorkFoundResponse](value)
        case Some("not_found") => Json.readJson[WorkNotFoundResponse](value)
        case _                 => ArchiveApiError(400, "Cannot parse response into a valid Work object")
      }
    }
  }

}
