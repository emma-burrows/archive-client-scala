package otw.api.utils

import org.specs2.mutable.Specification

case class Foo(camelCase: List[String])

class JsonTest extends Specification {

  "JsonTest" should {
    "writeJson" in {
      val thing = Foo(List("foo", "bar"))
      Json.writeJson(thing) should_=== """{"camel_case":["foo","bar"]}"""
    }

    "readJson for String" in {
      val thing = Json.readJson[Foo]("""{ "camel_case": ["foo","bar"] }""")
      thing should_== Foo(List("foo", "bar"))
    }
  }
}
