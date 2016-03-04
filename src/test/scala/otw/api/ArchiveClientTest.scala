package otw.api

import java.nio.charset.Charset

import org.mockito.Matchers.{any => anyArg, eq => argEq}
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification

import scala.concurrent.Await
import scala.concurrent.duration._

class ArchiveClientTest extends Specification with Mockito {

  "ArchiveClientTest" should {
    "createItems" in new HttpMockScope {
      val archiveClientWithMock = ArchiveClient("foo", "foo", Some(works))
      val undertest = Await.result(
        archiveClientWithMock.createWorks("archivist",
                                          sendClaimEmails = false,
                                          postWithoutPreview = false,
                                          Charset.forName("UTF-8"),
                                          "",
                                          List(work)), 1 second)
      undertest should_== Right(expected)
    }

    "findUrls" in {
      ok
    }

  }
}
