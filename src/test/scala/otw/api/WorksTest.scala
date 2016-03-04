package otw.api

import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import otw.api.request.WorkItem
import otw.api.response.FindUrlResponse

import scala.concurrent.Await
import scala.concurrent.duration._

class WorksTest extends Specification with Mockito {

  "WorksTest" should {
    "createItems" in new HttpMockScope {
      Await.result(works.createItems(WorkItem, createRequest), 1 second) should_== Right(expected)
    }

    "checkUrls" in new HttpMockScope {
      Await.result(works.checkUrls(List("foo")), 1 second) should_== Right(FindUrlResponse(200, List(workfound)))
    }

  }
}
