package com.campspot.lib;

import com.campspot.api.Punch;
import com.campspot.dao.PunchDAO;
import org.joda.time.DateTime;

import java.util.List;

public class PunchLib {
  private final PunchDAO punchDAO;

  public PunchLib(PunchDAO punchDAO) {
    this.punchDAO = punchDAO;
  }

  public Punch create(Punch punch) {
    validatePunchDoesNotOverlap(punch);

    Long id = punchDAO.create(punch.getStart(), punch.getEnd(), punch.getCategory(), punch.getDescription());
    return punchDAO.findById(id);
  }

  public Punch update(Punch punch) {
    validatePunchDoesNotOverlap(punch);

    punchDAO.update(punch.getId(), punch.getStart(), punch.getEnd(), punch.getCategory(), punch.getDescription());

    return punchDAO.findById(punch.getId());
  }

  public List<Punch> listForDates(DateTime start, DateTime end) {
    return punchDAO.listForDates(start, end);
  }

  private void validatePunchDoesNotOverlap(Punch punch) {
    if (punchDAO.anyInRange(punch.getStart(), punch.getEnd())) {
      throw new RuntimeException("Punch Cannot Overlap");
    }
  }
}
