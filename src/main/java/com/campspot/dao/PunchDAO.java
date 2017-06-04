package com.campspot.dao;

import com.campspot.api.Punch;
import org.joda.time.DateTime;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

import java.util.List;

public interface PunchDAO {
  @SqlUpdate("INSERT INTO Punch (start, end, category, description) VALUES (:start, :end, :category, :description)")
  @GetGeneratedKeys
  Long create(DateTime start, DateTime end, String category, String description);

  @SqlUpdate("UPDATE Punch SET start = :start, end = :end, category = :category, description = :description WHERE id = :id")
  void update(Long id, DateTime start, DateTime end, String category, String description);

  @SqlQuery("SELECT * FROM Punch WHERE start >= :start and end <= :end")
  List<Punch> listForDates(DateTime start, DateTime end);

  @SqlQuery("SELECT COUNT(id) > 0 FROM Punch WHERE start >= :start and end <= :end")
  Boolean anyInRange(DateTime start, DateTime end);

  @SqlQuery("SELECT * FROM Punch WHERE id = :id")
  Punch findById(Long id);
}
