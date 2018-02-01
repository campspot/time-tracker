package com.campspot.lib

import com.campspot.api.Punch
import com.campspot.dao.PunchDAO
import com.campspot.exceptions.EntityNotFoundException
import com.campspot.exceptions.PunchCannotOverlapException
import java.time.ZonedDateTime

class PunchLib(
  private val punchDAO: PunchDAO,
  private val mockableObject: MockableObject
) {
  fun create(punch: Punch): Punch {
    validatePunchDoesNotOverlap(punch)

    val id = punchDAO.create(punch)
    return punchDAO.findById(id)
  }

  fun update(punch: Punch): Punch {
    validatePunchDoesNotOverlap(punch)

    if (punch.id == null) {
      throw EntityNotFoundException("Punch must have ID to update value")
    }

    punchDAO.update(punch)
    return punchDAO.findById(punch.id)
  }

  fun listForDates(start: ZonedDateTime, end: ZonedDateTime): List<Punch> {
    return punchDAO.findAllInDateRange(start, end)
  }

  fun somethingForTesting(category: String): String {
    val firstForCategory = punchDAO.findFirstForCategory(category)

    return firstForCategory.description
  }

  fun anotherThingToTest(): String {
    return mockableObject.doStuff("whoop")
  }

  private fun validatePunchDoesNotOverlap(punch: Punch) {
    if (punchDAO.anyInRange(punch.start, punch.end)) {
      throw PunchCannotOverlapException()
    }
  }
}

