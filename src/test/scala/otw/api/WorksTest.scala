package otw.api

import java.text.SimpleDateFormat

import org.joda.time.DateTime
import org.json4s.native.JsonMethods._
import org.json4s.native.Serialization._
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import org.specs2.specification.Scope
import org.mockito.Matchers.{any => anyArg, eq => argEq, _}
import otw.api.request.OriginalUrls
import otw.api.response.{WorkFoundResponse, FindUrlResponse}
import otw.api.utils.{Json, HttpStatusWithJsonBody, ArchiveHttp}

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._

class WorksTest extends Specification with Mockito {

  trait httpMockScope extends Scope {

    import org.json4s._

    implicit val formats = new DefaultFormats {
      override val dateFormatter = new SimpleDateFormat("yyyy-MM-dd")
    } ++ org.json4s.ext.JodaTimeSerializers.all

    implicit val ec = ExecutionContext.global
    val httpMock = mock[ArchiveHttp]

    val workfound = WorkFoundResponse("ok", "foo", "bar", DateTime.parse("1903-05-12"))

    httpMock.post(anyString, argEq(Json.writeJson(OriginalUrls(List("foo")))))
      .returns {
        Future {
          Right(
            HttpStatusWithJsonBody(200, parse(write(List(workfound))))
          )
        }
      }

    // httpMock.post(anyString, anyString) returns Future(Right(HttpStatusWithJsonBody(200, JString("foo"))))

    val works = Works("token", "url", Some(httpMock))
  }

  "WorksTest" should {
    "createWorks" in {
      ok
    }.pendingUntilFixed("to be done")

    "checkUrls" in new httpMockScope {
      Await.result(works.checkUrls(List("foo")), 1 second) should_== Right(FindUrlResponse(200, List(workfound)))
    }

  }
}
