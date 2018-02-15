package com.campspot.api

import org.junit.Test
import java.time.ZonedDateTime

class PunchTest {
  private val punch = Punch(
    start = ZonedDateTime.parse("2016-10-05T06:45:00Z[UTC]"),
    end = ZonedDateTime.parse("2016-10-05T07:30:00Z[UTC]"),
    category = "Activities",
    description = "Party rocking",
    isPaid = true
  )

  @Test
  fun serializePunch() {
    APIObjectTester.testSerialization(punch, "fixtures/punch.json")
  }

  @Test
  fun deserializePunch() {
    APIObjectTester.testDeserialization(punch, "fixtures/punch.json")
  }
}
