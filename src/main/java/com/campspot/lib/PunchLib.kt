package com.campspot.lib

import com.campspot.api.Punch
import com.campspot.dao.PunchDAO
import org.joda.time.DateTime

class PunchLib(private val punchDAO: PunchDAO) {
  fun create(punch: Punch): Punch {
    validatePunchDoesNotOverlap(punch)

    val id = punchDAO.create(punch.start, punch.end, punch.category, punch.description)
    return punchDAO.findById(id)
  }

  fun update(punch: Punch): Punch {
    validatePunchDoesNotOverlap(punch)

    punchDAO.update(punch.id, punch.start, punch.end, punch.category, punch.description)

    return punchDAO.findById(punch.id)
  }

  fun listForDates(start: DateTime, end: DateTime): List<Punch> {
    return punchDAO.listForDates(start, end)
  }

  private fun validatePunchDoesNotOverlap(punch: Punch) {
    if (punchDAO.anyInRange(punch.start, punch.end)!!) {
      throw RuntimeException("Punch Cannot Overlap")
    }
  }
}
