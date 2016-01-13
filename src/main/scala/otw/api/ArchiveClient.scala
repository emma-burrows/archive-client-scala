package otw.api

import org.json4s.TypeHints
import org.json4s.NoTypeHints
import org.json4s.native.Serialization

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

case class ArchiveClient(archive_token: String,
                         archive_api_url: String) {

  implicit val formats = Serialization.formats(NoTypeHints)
  private val works = Works(archive_token, archive_api_url)

  def checkUrls(urls: List[String]) = works.checkUrls(urls)

  def checkUrlsJson(urls: List[String]) = works.checkUrlsJson(urls)

}
