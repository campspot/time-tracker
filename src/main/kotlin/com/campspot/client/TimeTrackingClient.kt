package com.campspot.client

import campspot.client.BaseClient
import com.campspot.api.Punch
import org.slf4j.LoggerFactory
import java.time.ZonedDateTime
import java.util.concurrent.Future
import javax.ws.rs.client.Client
import javax.ws.rs.core.GenericType
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.UriBuilder


class TimeTrackingClient(
  private val protocol: String,
  private val host: String,
  private val port: Int,
  private val client: Client,
  entityValidator: EntityValidator
) : BaseClient(LoggerFactory.getLogger(TimeTrackingClient::class.java), entityValidator) {

  private fun apiURL(path: String): UriBuilder {
    return UriBuilder.fromPath("/api" + path)
      .scheme(protocol)
      .host(host)
      .port(port)
  }

  fun createPunch(punch: Punch): Future<Punch> {
    val response = client.target(apiURL("/punches"))
      .request()
      .accept(MediaType.APPLICATION_JSON)
      .buildPost(validateJSON(punch))
      .submit()

    return checkAndTransformResponse(response, Punch::class.java)
  }

  fun updatePunch(punch: Punch): Future<Punch> {
    val response = client.target(apiURL("/punches"))
      .request()
      .accept(MediaType.APPLICATION_JSON)
      .buildPut(validateJSON(punch))
      .submit()

    return checkAndTransformResponse(response, Punch::class.java)
  }

  fun listPunches(start: ZonedDateTime, end: ZonedDateTime): Future<List<Punch>> {
    val response = client.target(apiURL("/punches"))
      .queryParam("start", start)
      .queryParam("end", end)
      .request()
      .accept(MediaType.APPLICATION_JSON)
      .buildGet()
      .submit()

    return checkAndTransformResponse(response, object : GenericType<List<Punch>>() {})
  }
}

