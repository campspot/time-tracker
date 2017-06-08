package integration

import com.campspot.api.Punch
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import uy.klutter.db.jdbi.v2.map
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

class PunchResourceTest : IntegrationTest() {
  @Test
  fun createPunch() {
    val start = ZonedDateTime.now(ZoneId.of("UTC"))
      .truncatedTo(ChronoUnit.DAYS)
      .plusDays(5)
      .withHour(11)
      .withMinute(15)
    val end = start.withMinute(45)
    val punch = Punch(start = start, end = end, category = "Stuff", description = "Very important, pay me extra")

    val createdPunch = apiClient!!.createPunch(punch).get()

    assertThat(createdPunch.id).isNotNull()

    val handle = jdbi!!.open()
    val punches = handle.createQuery("SELECT * FROM Punch").map(Punch::class).list()

    assertThat(punches).hasSize(1)
    assertThat(punches[0]).isEqualTo(createdPunch)
  }

  @Test
  fun updatePunch() {
    val id = 30L
    val start = ZonedDateTime.now(ZoneId.of("UTC"))
      .truncatedTo(ChronoUnit.DAYS)
      .plusDays(5)
      .withHour(11)
      .withMinute(15)
    val end = start.withMinute(45)
    val category = "Stuff"
    val description = "Very important, pay me extra"
    val punch = Punch(id = id, start = start, end = end, category = category, description = description)

    val handle = jdbi!!.open()
    handle.execute("INSERT INTO PUNCH (id, start, end, category, description) VALUES (?, ?, ?, ?, ?)", id, start, end, category, description)

    val newStart = start.plusDays(1)
    val newEnd = end.plusDays(1)
    val newCategory = "Things"
    val newDescription = "Party supplies"
    val updatedPunch = punch.copy(start = newStart, end = newEnd, category = newCategory, description = newDescription)

    val update = apiClient!!.updatePunch(updatedPunch).get()

    assertThat(update).isEqualTo(updatedPunch)

    val punchFromDB = handle
      .createQuery("SELECT * FROM Punch WHERE id = :id")
      .bind("id", punch.id)
      .map(Punch::class)
      .first()

    assertThat(update).isEqualTo(punchFromDB)
  }

  @Test
  fun listPunchesForTimeRange() {
    val rangeStart = ZonedDateTime.now(ZoneId.of("UTC"))
      .truncatedTo(ChronoUnit.DAYS)
      .plusDays(5)
    val rangeEnd = rangeStart.plusDays(5)

    val basePunch = Punch(id = 0, start = rangeStart, end = rangeEnd, category = "", description = "")

    val beforeStartNoOverlap = basePunch.copy(id = 1, start = rangeStart.minusDays(5), end = rangeStart.minusDays(3))
    val beforeStartTillExactlyStart = basePunch.copy(id = 2, start = rangeStart.minusDays(5), end = rangeStart)
    val beforeStartTillPastStart = basePunch.copy(id = 3, start = rangeStart.minusDays(2), end = rangeStart.plusDays(1))
    val beforeStartTillAfterEnd = basePunch.copy(id = 4, start = rangeStart.minusDays(3), end = rangeEnd.plusDays(2))
    val exactlyStartTillBeforeEnd = basePunch.copy(id = 5, start = rangeStart, end = rangeEnd.minusDays(1))
    val exactlyStartTillExactlyEnd = basePunch.copy(id = 6, start = rangeStart, end = rangeEnd)
    val exactlyStartTillAfterEnd = basePunch.copy(id = 7, start = rangeStart, end = rangeEnd.plusDays(3))
    val afterStartTillBeforeEnd = basePunch.copy(id = 8, start = rangeStart.plusDays(1), end = rangeEnd.minusDays(1))
    val afterStartTillExactlyEnd = basePunch.copy(id = 9, start = rangeStart.plusDays(3), end = rangeEnd)
    val afterStartTillAfterEnd = basePunch.copy(id = 10, start = rangeStart.plusDays(4), end = rangeEnd.plusDays(2))
    val exactlyEndToAfterEnd = basePunch.copy(id = 11, start = rangeEnd, end = rangeEnd.plusDays(1))
    val afterEndNoOverlap = basePunch.copy(id = 12, start = rangeEnd.plusDays(2), end = rangeEnd.plusDays(3))

    val handle = jdbi!!.open()
    handle.prepareBatch("INSERT INTO Punch (id, start, end, category, description) VALUES (?, ?, ?, ?, ?)")
      .add(beforeStartNoOverlap.id, beforeStartNoOverlap.start, beforeStartNoOverlap.end, "", "")
      .add(beforeStartTillExactlyStart.id, beforeStartTillExactlyStart.start, beforeStartTillExactlyStart.end, "", "")
      .add(beforeStartTillPastStart.id, beforeStartTillPastStart.start, beforeStartTillPastStart.end, "", "")
      .add(beforeStartTillAfterEnd.id, beforeStartTillAfterEnd.start, beforeStartTillAfterEnd.end, "", "")
      .add(exactlyStartTillBeforeEnd.id, exactlyStartTillBeforeEnd.start, exactlyStartTillBeforeEnd.end, "", "")
      .add(exactlyStartTillExactlyEnd.id, exactlyStartTillExactlyEnd.start, exactlyStartTillExactlyEnd.end, "", "")
      .add(exactlyStartTillAfterEnd.id, exactlyStartTillAfterEnd.start, exactlyStartTillAfterEnd.end, "", "")
      .add(afterStartTillBeforeEnd.id, afterStartTillBeforeEnd.start, afterStartTillBeforeEnd.end, "", "")
      .add(afterStartTillExactlyEnd.id, afterStartTillExactlyEnd.start, afterStartTillExactlyEnd.end, "", "")
      .add(afterStartTillAfterEnd.id, afterStartTillAfterEnd.start, afterStartTillAfterEnd.end, "", "")
      .add(exactlyEndToAfterEnd.id, exactlyEndToAfterEnd.start, exactlyEndToAfterEnd.end, "", "")
      .add(afterEndNoOverlap.id, afterEndNoOverlap.start, afterEndNoOverlap.end, "", "")
      .execute()

    val searchResults = apiClient!!.listPunches(rangeStart, rangeEnd).get()

    assertThat(searchResults).hasSize(8)
    assertThat(searchResults.map { (id) -> id }).containsExactlyInAnyOrder(3, 4, 5, 6, 7, 8, 9, 10)
  }
}
