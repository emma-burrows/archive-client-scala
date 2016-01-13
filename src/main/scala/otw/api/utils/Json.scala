package otw.api.utils

import java.text.SimpleDateFormat

import org.json4s.native.JsonMethods._
import org.json4s.native.Serialization
import org.json4s.native.Serialization._

private[api] class Json {
  import org.json4s._
  implicit val formats = new DefaultFormats {
    override val dateFormatter = new SimpleDateFormat("yyyy-MM-dd")
  } ++ org.json4s.ext.JodaTimeSerializers.all

  def writeJson[A <: AnyRef](a: A) =
    compact(render(parse(write(a)).snakizeKeys))

  def readJson[A <: AnyRef](s: String)(implicit manifest: Manifest[A]): A =
    parse(s).camelizeKeys.extract[A]

  def readJson[A <: AnyRef](s: JValue)(implicit manifest: Manifest[A]): A =
    readJson[A](compact(render(s)))
}
