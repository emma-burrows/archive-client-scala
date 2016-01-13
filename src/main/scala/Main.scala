import otw.api.{Works, ArchiveClient}
import dispatch._
import Defaults._
import org.json4s.NoTypeHints
import org.json4s.native.Serialization
import org.json4s.native.Serialization.{write => swrite}
import otw.api.response.WorkCheckResponse

import otw.api.utils._

import scala.util.Properties

import scala.util.{Success, Failure}

object Main extends App {

  override def main(args: Array[String]) {
    implicit val formats = Serialization.formats(NoTypeHints)
    val archive_token = Properties.propOrElse("archiveToken", "1234")
    val archive_api_url = Properties.propOrElse("archiveApiHost", "archiveofourown.org")

    val works = ArchiveClient(archive_token, archive_api_url)

    works.checkUrls(List("http://astele.co.uk/other/ao3.html", "bar")) map {
      case Right(resp) =>
        println("\n\nTHING\n" + resp)
        // resp.map(r:WorkCheckResponse => s"status: ${r.status.map(_)}\n")
      case Left(ex) =>
        println("\n\nERROR\n" + ex)
    }

    works.checkUrlsJson(List("http://astele.co.uk/other/ao3.html", "bar")) map { case resp =>
      println("JSON " + resp)
    }

  }
}

