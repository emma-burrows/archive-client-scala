package otw.api
import java.text.SimpleDateFormat

import org.joda.time.DateTime
import org.json4s.DefaultFormats
import org.json4s.native.JsonMethods._
import org.json4s.native.Serialization._
import org.specs2.matcher.ThrownExpectations
import org.specs2.specification.Scope
import org.specs2.mock.Mockito
import otw.api.request.{OriginalUrls, CreateRequest, Work}
import otw.api.response.{CreateResponse, ItemCreateResponse, WorkFoundResponse}
import otw.api.utils.{HttpStatusWithJsonBody, Json, ArchiveHttp}
import org.mockito.Matchers.{any => anyArg, eq => argEq}

import scala.concurrent.{Future, ExecutionContext}

trait HttpMockScope extends Scope with Mockito with ThrownExpectations {

  implicit val formats = new DefaultFormats {
    override val dateFormatter = new SimpleDateFormat("yyyy-MM-dd")
  } ++ org.json4s.ext.JodaTimeSerializers.all

  implicit val ec = ExecutionContext.global


  val workfound = WorkFoundResponse("ok", "foo", "bar", DateTime.parse("1903-05-12"))
  val items = List(Work("original-url", "author", "email", "Title", "Summary", "Fandom", "Explicit", "M/M", "Mulder/Scully", "Dana Scully"))

  val expectedItems = List(ItemCreateResponse("ok", "archive-url", "original-url", List("messages")))
  val expected = CreateResponse("ok", List("message"), expectedItems)
  val createRequest =
    CreateRequest("archivist", sendClaimEmails = false, postWithoutPreview = false, "UTF-8", "CollectionNames", items)

  val archiveClient = ArchiveClient("foo", "foo")
  val work = Work("url", "Author", "email", "Title", "summary", "Fandom", "General", "F/M", "X/Y", "X")

  val httpMock = mock[ArchiveHttp]

  val works = Works("token", "url", Some(httpMock))

  httpMock.post(argEq("api/v1/works/urls"), argEq(Json.writeJson(OriginalUrls(List("foo")))))
    .returns {
      Future {
        Right(HttpStatusWithJsonBody(200, parse(write(List(workfound)))))
      }
    }

  httpMock.post(argEq("api/v1/import/"), any)
    .returns {
      Future {
        Right(HttpStatusWithJsonBody(200, parse(write(expected))))
      }
    }

}
