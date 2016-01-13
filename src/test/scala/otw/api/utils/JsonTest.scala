package otw.api.utils

import org.json4s._
import org.json4s.native._
import org.specs2.mutable.Specification

case class Foo(camelCase: List[String])

class JsonTest extends Specification {
  val json = new Json

  "JsonTest" should {
    "writeJson" in {
      val thing = Foo(List("foo", "bar"))
      json.writeJson(thing) should_== """{ "camel_case": ["foo","bar"] }"""
    }

    "readJson for String" in {
      val thing = json.readJson[Foo]("""{ "camel_case": ["foo","bar"] }""")
      thing should_== Foo(List("foo", "bar"))
    }

    "readJson for JsValue" in {
      val thing = json.readJson[Foo](parseJson("""{ "camel_case": ["foo","bar"] }"""))
      thing should_== Foo(List("foo", "bar"))
    }
  }
}
