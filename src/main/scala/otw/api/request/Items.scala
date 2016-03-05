package otw.api.request

sealed trait Item

case class Work(url: String,
                author: String,
                email: String,
                title: String,
                summary: String,
                fandomString: String,
                ratingString: String,
                categoryString: String,
                relationshipString: String,
                characterString: String,
                chapter_urls: List[String])
          extends Item

case class Bookmark(url: String,
                    author: String,
                    email: String,
                    title: String,
                    summary: String,
                    fandomString: String,
                    ratingString: String,
                    categoryString: String,
                    relationshipString: String,
                    characterString: String)
  extends Item


sealed abstract class ItemType
case object BookmarkItem extends ItemType
case object WorkItem extends ItemType
