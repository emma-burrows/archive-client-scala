package otw.api.utils

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock._
import com.github.tomakehurst.wiremock.core.WireMockConfiguration._
import org.json4s.JsonAST.{JArray, JString}
import org.specs2.mutable.Specification
import org.specs2.specification.BeforeAfterAll
import scala.concurrent.duration._

import scala.concurrent.Await

class ArchiveHttpTest extends Specification with BeforeAfterAll {

  val Host = "localhost"
  val Port = 8888

  val archiveHttp = ArchiveHttp("123", s"$Host:$Port")
  val wireMockServer = new WireMockServer(wireMockConfig().port(Port))

  def beforeAll() = {
    if (wireMockServer.isRunning) wireMockServer.stop()
    wireMockServer.start()
    WireMock.configureFor(Host, Port)

    // Stub out the creative otw.api.response
    stubFor(get(urlMatching("/works"))
              .willReturn(
                aResponse()
                  .withStatus(200)
                  .withBody("""["url1", "url2"]""")))

    // Stub out the creative otw.api.response
    stubFor(post(urlMatching("/works/urls"))
              .willReturn(
                aResponse()
                  .withStatus(200)
                  .withBody("""["url1", "url2"]""")))
  }

  def afterAll() = {}

  "ArchiveHttpTest" should {
    "get" in {
      val thing = Await.result(archiveHttp.get("works"), 500 millis)
      thing should_== Right(HttpStatusWithJsonBody(200, JArray(List(JString("url1"), JString("url2")))))
    }

    "post" in {
      val thing = Await.result(archiveHttp.post("works/urls", "url1, url2"), 500 millis)
      thing should_== Right(HttpStatusWithJsonBody(200, JArray(List(JString("url1"), JString("url2")))))
    }

  }
}
