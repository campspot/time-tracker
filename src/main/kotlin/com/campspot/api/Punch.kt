package com.campspot.api

import java.time.ZonedDateTime

data class Punch(
  val id: Long? = null,
  val start: ZonedDateTime,
  val end: ZonedDateTime,
  val category: String,
  val description: String
)
