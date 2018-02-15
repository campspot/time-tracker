package com.campspot.lib

import com.campspot.api.Punch
import com.campspot.dao.PunchDAO
import com.campspot.exceptions.EntityNotFoundException
import com.campspot.exceptions.PunchCannotOverlapException
import com.campspot.jdbi3.DAOManager
import java.time.ZonedDateTime

class PunchLib(
  private val daoManager: DAOManager,
  private val mockableObject: MockableObject
) {
  fun create(punch: Punch): Punch {
    val punchDAO = daoManager[PunchDAO::class]
    validatePunchDoesNotOverlap(punch)

    val id = punchDAO.create(punch)
    return punchDAO.findById(id)
  }

  fun update(punch: Punch): Punch {
    val punchDAO = daoManager[PunchDAO::class]
    validatePunchDoesNotOverlap(punch)

    if (punch.id == null) {
      throw EntityNotFoundException("Punch must have ID to update value")
    }

    punchDAO.update(punch)
    return punchDAO.findById(punch.id)
  }

  fun listForDates(start: ZonedDateTime, end: ZonedDateTime): List<Punch> {
    val punchDAO = daoManager[PunchDAO::class]
    return punchDAO.findAllInDateRange(start, end)
  }

  fun somethingForTesting(category: String): String {
    val punchDAO = daoManager[PunchDAO::class]
    val firstForCategory = punchDAO.findFirstForCategory(category)

    return firstForCategory.description
  }

  fun anotherThingToTest(): String {
    return mockableObject.doStuff("whoop")
  }

  private fun validatePunchDoesNotOverlap(punch: Punch) {
    val punchDAO = daoManager[PunchDAO::class]
    if (punchDAO.anyInRange(punch.start, punch.end)) {
      throw PunchCannotOverlapException()
    }
  }
}

