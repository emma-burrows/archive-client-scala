package otw.api

import java.nio.charset.Charset

import org.json4s.NoTypeHints
import org.json4s.native.Serialization
import otw.api.request.{WorkItem, Item, CreateRequest}

import scala.concurrent.ExecutionContext

case class ArchiveClient(archive_token: String,
                         archive_api_url: String) {

  implicit val formats = Serialization.formats(NoTypeHints)
  private val worksClient = Works(archive_token, archive_api_url)

  def findUrls(urls: List[String])(implicit ec: ExecutionContext) = worksClient.checkUrls(urls)

  def createWorks(archivist: String,
                  sendClaimEmails: Boolean,
                  postWithoutPreview: Boolean,
                  encoding: Charset,
                  collectionNames: String,
                  works: List[Item])(implicit ec: ExecutionContext) = {
    val charset = encoding.displayName
    val settings = CreateRequest(archivist, sendClaimEmails, postWithoutPreview, charset, collectionNames, works)
    worksClient.createItems(WorkItem, settings)
  }

}
