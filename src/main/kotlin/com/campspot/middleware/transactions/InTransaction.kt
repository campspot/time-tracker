package com.campspot.middleware.transactions

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class InTransaction(val name: String)
