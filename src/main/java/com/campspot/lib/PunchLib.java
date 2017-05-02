package com.campspot.lib;

import com.campspot.api.Punch;
import com.campspot.dao.PunchDAO;
import com.campspot.lib.mappers.PunchMapper;
import com.google.common.collect.ImmutableList;
import org.joda.time.DateTime;

public class PunchLib {
  private final PunchDAO punchDAO;

  public PunchLib(PunchDAO punchDAO) {
    this.punchDAO = punchDAO;
  }

  public Punch create(Punch punch) {
    return PunchMapper.fromModel(punchDAO.create(PunchMapper.fromAPI(punch)));
  }

  public ImmutableList<Punch> listForDates(DateTime start, DateTime end) {
    return punchDAO.listForDates(start, end)
      .stream()
      .map(PunchMapper::fromModel)
      .collect(ImmutableList.toImmutableList());
  }
}
