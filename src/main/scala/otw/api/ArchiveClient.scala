package otw.api

import java.nio.charset.Charset

import org.json4s.NoTypeHints
import org.json4s.native.Serialization
import otw.api.request._

import scala.concurrent.ExecutionContext

case class ArchiveClient(archive_token: String,
                         archive_api_url: String,
                         worksOpt: Option[Works] = None) {

  implicit val formats = Serialization.formats(NoTypeHints)
  private val worksClient: Works = worksOpt.map{ case w: Works => w }.getOrElse(Works(archive_token, archive_api_url))

  def findUrls(request: FindWorkRequest)(implicit ec: ExecutionContext) = worksClient.checkUrls(request)

  def createWorks(archivist: String,
                  sendClaimEmails: Boolean,
                  postWithoutPreview: Boolean,
                  encoding: Charset,
                  collectionNames: String,
                  works: List[Item])(implicit ec: ExecutionContext) = {
    val charset = if (encoding.displayName.nonEmpty) { encoding.displayName } else "UTF-8"
    val settings = CreateRequest(archivist, sendClaimEmails, postWithoutPreview, charset, collectionNames, works)
    worksClient.createItems(WorkItem, settings)
  }

}
