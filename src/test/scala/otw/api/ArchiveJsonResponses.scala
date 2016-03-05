package otw.api

object ArchiveJsonResponses {

  val createError =
    """
      |{
      |    "status": "unprocessable_entity",
      |    "messages": [
      |        "None of the works were imported. Please check the works array for further information."
      |    ],
      |    "works": [
      |        {
      |            "status": "unprocessable_entity",
      |            "url": "",
      |            "original_url": "http://astele.co.uk/wolf/Chapter/Details/535",
      |            "messages": [
      |                "\"backtrace must be Array of String\""
      |            ]
      |        }
      |    ]
      |}
    """.stripMargin

  val successful =
    """
      |
    """.stripMargin

}
