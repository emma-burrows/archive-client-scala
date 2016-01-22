package otw.api

import org.json4s.NoTypeHints
import org.json4s.native.Serialization

import scala.concurrent.ExecutionContext

case class ArchiveClient(archive_token: String,
                         archive_api_url: String) {

  implicit val formats = Serialization.formats(NoTypeHints)
  private val works = Works(archive_token, archive_api_url)

  def findUrls(urls: List[String])(implicit ec: ExecutionContext) = works.checkUrls(urls)

  def createWorks()(implicit ec: ExecutionContext) = works.createWorks()

}
