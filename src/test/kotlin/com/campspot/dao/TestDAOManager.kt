package com.campspot.dao

import com.campspot.middleware.transactions.DAOManager
import org.mockito.Mockito
import kotlin.reflect.KClass

class TestDAOManager: DAOManager() {
  private val mocks = HashMap<KClass<out DAO>, DAO>()

  override operator fun <T: DAO> get(dao: KClass<T>): T {
    if (!mocks.containsKey(dao)) {
      mocks[dao] = Mockito.mock(dao.java)
    }

    @Suppress("UNCHECKED_CAST")
    return mocks[dao] as T
  }
}
