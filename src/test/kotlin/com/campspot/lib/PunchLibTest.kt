package com.campspot.lib

import com.campspot.any
import com.campspot.api.Punch
import com.campspot.dao.PunchDAO
import com.campspot.exceptions.EntityNotFoundException
import com.campspot.exceptions.PunchCannotOverlapException
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.Mockito.*
import java.time.ZonedDateTime

class PunchLibTest {
  val punchDAO = mock(PunchDAO::class.java)
  val mockableObject = mock(MockableObject::class.java)
  val subject = PunchLib(punchDAO, mockableObject)

  val start = ZonedDateTime.now()
  val end = ZonedDateTime.now().plusMinutes(30)
  val basicPunch = Punch(start = start, end = end, category = "", description = "")

  @Test(expected = PunchCannotOverlapException::class)
  fun create_ThrowsErrorIfPunchOverlaps() {
    `when`(punchDAO.anyInRange(start, end)).thenReturn(true)

    subject.create(basicPunch)
  }

  @Test
  fun create_fetchesAndReturnsNewlyCreatedPunch() {
    val id = 5L
    val punchWithId = basicPunch.copy(id = id)

    `when`(punchDAO.anyInRange(start, end)).thenReturn(false)
    `when`(punchDAO.create(basicPunch)).thenReturn(id)
    `when`(punchDAO.findById(id)).thenReturn(punchWithId)

    val createdPunch = subject.create(basicPunch)

    assertThat(createdPunch).isEqualTo(punchWithId)
  }

  @Test(expected = PunchCannotOverlapException::class)
  fun update_ThrowsErrorIfPunchesOverlap() {
    `when`(punchDAO.anyInRange(start, end)).thenReturn(true)

    subject.update(basicPunch)
  }

  @Test(expected = EntityNotFoundException::class)
  fun update_ThrowsErrorIfPunchIsMissingID() {
    `when`(punchDAO.anyInRange(start, end)).thenReturn(false)

    subject.update(basicPunch)
  }

  @Test
  fun update_returnsTheUpdatedPunch() {
    val id = 5L
    val punchWithId = basicPunch.copy(id = id)

    `when`(punchDAO.anyInRange(start, end)).thenReturn(false)
    `when`(punchDAO.findById(id)).thenReturn(punchWithId)

    val createdPunch = subject.update(punchWithId)

    assertThat(createdPunch).isEqualTo(punchWithId)
  }

  @Test
  fun `you can have better test names like this`() {
    val category = "whoop"
    val description = "described"
    val punchWithDescription = basicPunch.copy(category = category, description = description)

    `when`(punchDAO.findFirstForCategory(category)).thenReturn(punchWithDescription)

    assertThat(subject.somethingForTesting(category)).isEqualTo(description)
  }

  @Test
  fun `testing another mocking thing`() {
    `when`(mockableObject.doStuff("whoop")).thenReturn("de-doop")

    assertThat(subject.anotherThingToTest()).isEqualTo("de-doop")

    verify(mockableObject, times(1)).doStuff("whoop")
    verify(mockableObject, times(0)).doStuff("Whoop")
  }

  @Test
  fun `using mockito's any() method has issues so we made our own`() {
    val category = "whoop"
    val description = "described"
    val punchWithDescription = basicPunch.copy(category = category, description = description)

    `when`(punchDAO.findFirstForCategory(any(String::class))).thenReturn(punchWithDescription)

    assertThat(subject.somethingForTesting(category)).isEqualTo(description)
  }
}
