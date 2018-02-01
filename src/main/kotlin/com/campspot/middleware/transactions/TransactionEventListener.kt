package com.campspot.middleware.transactions

import org.glassfish.jersey.server.internal.process.MappableException
import org.glassfish.jersey.server.model.ResourceMethod
import org.glassfish.jersey.server.monitoring.ApplicationEvent
import org.glassfish.jersey.server.monitoring.ApplicationEventListener
import org.glassfish.jersey.server.monitoring.RequestEvent
import org.glassfish.jersey.server.monitoring.RequestEventListener
import org.jdbi.v3.core.Jdbi
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import javax.ws.rs.ext.Provider

@Provider
class TransactionApplicationListener(private val daoManager: DAOManager) : ApplicationEventListener {
  private val methodMap = ConcurrentHashMap<ResourceMethod, InTransaction?>()
  private val dbis = HashMap<String, Jdbi>()


  fun registerDbi(name: String, jdbi: Jdbi) {
    dbis[name] = jdbi
  }

  private class TransactionEventListener internal constructor(
    private val methodMap: ConcurrentMap<ResourceMethod, InTransaction?>,
    daoManager: DAOManager,
    dbis: Map<String, Jdbi>
  ) : RequestEventListener {
    private val transactionAspect: TransactionAspect = TransactionAspect(dbis, daoManager)

    override fun onEvent(event: RequestEvent) {
      val eventType = event.type
      if (eventType == RequestEvent.Type.RESOURCE_METHOD_START) {
        val inTransaction = methodMap.computeIfAbsent(event.uriInfo.matchedResourceMethod, { registerInTransactionAnnotations(it)})
        inTransaction?.let { transactionAspect.beforeStart(it) }
      } else if (eventType == RequestEvent.Type.RESP_FILTERS_START) {
        try {
          transactionAspect.afterEnd()
        } catch (e: Exception) {
          throw MappableException(e)
        }

      } else if (eventType == RequestEvent.Type.ON_EXCEPTION) {
        transactionAspect.onError()
      } else if (eventType == RequestEvent.Type.FINISHED) {
        transactionAspect.onFinish()
      }
    }

    private fun registerInTransactionAnnotations(method: ResourceMethod): InTransaction? {
      var annotation: InTransaction? = method.invocable.definitionMethod.getAnnotation(InTransaction::class.java)
      if (annotation == null) {
        annotation = method.invocable.handlingMethod.getAnnotation(InTransaction::class.java)
      }
      return annotation
    }
  }

  override fun onEvent(event: ApplicationEvent) {}

  override fun onRequest(event: RequestEvent): RequestEventListener {
    return TransactionEventListener(methodMap, daoManager, dbis)
  }
}

