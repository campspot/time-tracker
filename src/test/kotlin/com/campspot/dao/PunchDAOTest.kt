package com.campspot.dao

import com.campspot.api.Punch
import org.assertj.core.api.Assertions.assertThat
import org.jdbi.v3.sqlobject.kotlin.onDemand
import org.junit.Test
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

class PunchDAOTest : DAOTest() {
  val punchDAO = jdbi!!.onDemand<PunchDAO>()

  @Test
  fun anyInRange_returnsWhetherAnyExistingPunchesExistInRange() {
    val start = ZonedDateTime.now(ZoneId.of("UTC"))
      .truncatedTo(ChronoUnit.DAYS)
      .plusDays(5)
      .withHour(11)
    val end = start.plusMinutes(30)
    val punch = Punch(start = start, end = end, category = "", description = "", isPaid = true)

    punchDAO.create(punch)

    assertThat(punchDAO.anyInRange(start, end)).isTrue()
    assertThat(punchDAO.anyInRange(start.plusMinutes(1), end.minusMinutes(1))).isTrue()
    assertThat(punchDAO.anyInRange(start.minusMinutes(20), end.minusMinutes(10))).isTrue()
    assertThat(punchDAO.anyInRange(start.plusMinutes(20), end.plusMinutes(10))).isTrue()
    assertThat(punchDAO.anyInRange(start.minusMinutes(10), start)).isFalse()
    assertThat(punchDAO.anyInRange(end, end.plusMinutes(5))).isFalse()
  }
}
