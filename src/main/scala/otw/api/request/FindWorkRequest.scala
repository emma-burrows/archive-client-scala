package otw.api.request

case class FindWorkRequest(originalRefs: List[OriginalRef])

case class OriginalRef(id: String, url: String)
