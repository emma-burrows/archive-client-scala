import dispatch.Defaults._
import dispatch._
import org.json4s.NoTypeHints
import org.json4s.native.Serialization
import org.json4s.native.Serialization.{write => swrite}
import otw.api.ArchiveClient
import otw.api.request.{FindWorkRequest, OriginalRef}

import scala.util.Properties

object Main extends App {

  override def main(args: Array[String]) {
    implicit val formats = Serialization.formats(NoTypeHints)
    val archive_token = Properties.propOrElse("archiveToken", "1234")
    val archive_api_url = Properties.propOrElse("archiveApiHost", "archiveofourown.org")

    val works = ArchiveClient(archive_token, archive_api_url)

    works.findUrls(FindWorkRequest(List(OriginalRef("1", "http://astele.co.uk/other/ao3.html"), OriginalRef("2", "bar")))) map {
      case Right(resp) =>
        println("\n\nTHING\n" + resp)
      case Left(ex) =>
        println("\n\nERROR\n" + ex)
    }

  }
}

