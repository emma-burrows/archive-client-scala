package otw.api.request

sealed trait Item

case class Work(url: String,
                author: String,
                title: String,
                summary: String,
                fandomString: String,
                ratingString: String,
                CategoryString: String,
                RelationshipString: String,
                characterString: String)
          extends Item

case class Bookmark(url: String,
                author: String,
                title: String,
                summary: String,
                fandomString: String,
                ratingString: String,
                CategoryString: String,
                RelationshipString: String,
                characterString: String)
  extends Item



