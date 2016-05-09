package otw.api

import org.json4s.JValue
import org.json4s.JsonAST.{JString, JNothing}
import otw.api.request._
import otw.api.response._
import otw.api.utils.{HttpStatusWithJsonBody, ArchiveHttp, Json}

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

    val create: dispatch.Future[Either[Throwable, HttpStatusWithJsonBody]] = `type` match {
      case WorkItem => http.post(workPath, requestJson)

      case BookmarkItem => http.post(bookmarkPath, requestJson)
    }

    create map {
      case Right(resp) =>
        resp.status match {
          case status if (200 until 299) contains status =>
            if (resp.body != JNothing)
              Right(createResponse(resp.body))
            else
              Right(ArchiveApiError(resp.status, List("No information returned from remote server")))

          case status if (400 until 404) contains status =>
            val error = Json.readJson[CreateResponse](resp.body)
            Right(ArchiveApiError(resp.status, error.messages))

          case status  =>
            println("Other status: " + status)
            val error = Json.readJson[Error](resp.body.children.head)
            Right(ArchiveApiError(resp.status, List(error.error)))
        }

      case Left(ex) => println(ex); Left(ex)
    }
  }

  private def createResponse(value: JValue) = {
    val status = Json.readJson[Map[String, JValue]](value).get("status")
    status match {
      case Some(JString("ok")) | Some(JString("created")) => Json.readJson[CreateResponse](value)

      case Some(JString("forbidden")) =>
        val response = Json.readJson[CreateResponse](value)
        ArchiveApiError(403, response.messages)

      case Some(JString("bad_request")) =>
        val response = Json.readJson[CreateResponse](value)
        ArchiveApiError(400, response.messages)

      case _                                              =>
        ArchiveApiError(400, List("Cannot parse response into a valid Work object"))
    }
  }

  // Find works on the Archive
  def checkUrls(request: FindWorkRequest)(implicit ec: ExecutionContext): Future[Either[Throwable, ArchiveResponse]] = {
    val originalUrls = Json.writeJson(request)

    val check = http.post(checkUrlPath, originalUrls)

    check map {
      case Right(resp) =>
        println("resp in checkUrls: " + resp)
        resp.status match {
          case status if 200 until 299 contains status =>
            if (resp.body != JNothing)
              Right(FindWorkResponse(status, checkResponses(resp.body)))
            else
              Right(ArchiveApiError(resp.status, List("No information returned from remote server")))

          case status if 400 until 499 contains status =>
            val error = Json.readJson[Error](resp.body.children.head)
            Right(ArchiveApiError(resp.status, List(error.error)))
        }

      case Left(ex) => println(ex); Left(ex)
    }
  }

  private def checkResponses(body: JValue): List[ArchiveResponse] = {
    body.children.map { value =>
      val status = Json.readJson[Map[String, String]](value).get("status")
      status match {
        case Some("ok")        => Json.readJson[WorkFoundResponse](value)
        case Some("not_found") => Json.readJson[WorkNotFoundResponse](value)
        case _                 => ArchiveApiError(400, List("Cannot parse response into a valid Work object"))
      }
    }
  }

}
