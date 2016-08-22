package otw.api.request

sealed trait Item

case class Work(id: String,
                url: String,
                author: String,
                email: String,
                title: String,
                summary: String,
                fandomString: String,
                ratingString: String,
                categoryString: String,
                relationshipString: String,
                characterString: String,
                chapterUrls: List[String])
          extends Item

case class Bookmark(id: String,
                    url: String,
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
