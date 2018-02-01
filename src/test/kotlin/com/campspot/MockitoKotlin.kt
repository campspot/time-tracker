package com.campspot

import org.mockito.Mockito
import kotlin.reflect.KClass

fun <T: Any> any(kClass: KClass<T>): T {
  Mockito.any<T>(kClass.java)
  return uninitialized()
}

@Suppress("UNCHECKED_CAST")
private fun <T> uninitialized(): T = null as T
