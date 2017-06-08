package campspot.client

import com.campspot.client.EntityValidator
import com.google.common.util.concurrent.Futures
import org.slf4j.Logger
import java.util.concurrent.Future
import javax.ws.rs.client.Entity
import javax.ws.rs.core.GenericType
import javax.ws.rs.core.HttpHeaders
import javax.ws.rs.core.Response

open class BaseClient(protected val logger: Logger, protected val entityValidator: EntityValidator) {

  fun <T> validateJSON(entity: T): Entity<T> {
    return Entity.json<T>(entityValidator.validate(entity))
  }

  fun <T> unwrapFuture(future: Future<T>, errorMessage: String): T {
    try {
      return future.get()
    } catch (e: Exception) {
      logger.error(errorMessage, e)
      throw RuntimeException(errorMessage)
    }
  }

  protected fun checkResponse(response: Response?): Response {
    if (response == null) {
      throw Error("Why was the response null?")
    }

    val contentTypeHeader = response.getHeaderString(HttpHeaders.CONTENT_TYPE)
    if (contentTypeHeader != null && !contentTypeHeader.contains("charset")) {
      throw RuntimeException(String.format("Expected response to define a charset in its Content-Type header. Content-Type: ", contentTypeHeader))
    }

    return response
  }

  protected fun <T> checkAndTransformResponse(response: Future<Response>, ResponseType: Class<T>): Future<T> {
    return Futures.lazyTransform(response) { r -> entityValidator.validate(checkResponse(r).readEntity(ResponseType)) }
  }

  protected fun <T> checkAndTransformResponse(response: Future<Response>, responseTypeWrapper: GenericType<T>): Future<T> {
    return Futures.lazyTransform(response) { r -> entityValidator.validate(checkResponse(r).readEntity(responseTypeWrapper)) }
  }
}
