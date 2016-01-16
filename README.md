# archive-client-scala
Scala client for the Archive of our Own API

# Background
This client for the AO3 API is used by the Open Doors project to import works from external archives into Archive of our Own with minimal manual intervention.

# Requests
The client provides the following API methods (see below for possible responses):

- `findUrls(urls)` where `urls` is a list of external URLs as strings. This checks whether any works have already been imported from a list of urls.

All the public methods take an `asJson` flag that allows you to receive the response as a Json string instead.

# Responses
Internally, the client uses [Dispatch](http://dispatch.databinder.net/Dispatch.html) and [Json4s](https://github.com/json4s/json4s). 

All its public methods return a `Future` which when complete will contain an [`Either`](http://alvinalexander.com/scala/scala-either-left-right-example-option-some-none-null). The `Either` returned will be either (heh):

- A `Left[Throwable]`, where the `Throwable` is an exception that occurred while trying to connect to the remote Archive server. For example, if the client can't connect to the Archive at all.

- a `Right[ArchiveResponse]`, where the `ArchiveResponse` depends on the request made:
  - `ArchiveApiError`: the client got a valid response from the Archive API but the request was invalid for some reason, for example because fields were missing. The `status` field will tell you what HTTP response was returned, and the `error` field will typically contain a human-readable explanation for the failure.
  - `FindUrlResponse`: responses for the `findUrls` methods, which can be an `ArchiveApiError` if the request was invalid (for instance, no valid URL was provided), or a `WorkFoundResponse` or `WorkNotFoundResponse` for when a work imported from the provided URL was respectively found or not.
  
