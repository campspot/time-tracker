package com.campspot

import org.mockito.Mockito
import kotlin.reflect.KClass

// https://stackoverflow.com/a/30308199/2067277
fun <T: Any> any(kClass: KClass<T>): T {
  Mockito.any<T>(kClass.java)
  return uninitialized()
}

@Suppress("UNCHECKED_CAST")
private fun <T> uninitialized(): T = null as T
