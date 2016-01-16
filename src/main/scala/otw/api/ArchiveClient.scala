package otw.api

import org.json4s.NoTypeHints
import org.json4s.native.Serialization

case class ArchiveClient(archive_token: String,
                         archive_api_url: String) {

  implicit val formats = Serialization.formats(NoTypeHints)
  private val works = Works(archive_token, archive_api_url)

  def findUrls(urls: List[String], asJson: Boolean = false) = works.findUrls(urls, asJson)

}
