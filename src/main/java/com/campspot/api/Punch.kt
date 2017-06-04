package com.campspot.api

import org.joda.time.DateTime

data class Punch(
  val id: Long?,
  val start: DateTime,
  val end: DateTime,
  val category: String,
  val description: String
)
