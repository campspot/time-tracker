package com.campspot.dao

import com.campspot.api.Punch
import org.joda.time.DateTime
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys
import org.skife.jdbi.v2.sqlobject.SqlQuery
import org.skife.jdbi.v2.sqlobject.SqlUpdate

interface PunchDAO {
  @SqlUpdate("INSERT INTO Punch (start, end, category, description) VALUES (:start, :end, :category, :description)")
  @GetGeneratedKeys
  fun create(start: DateTime, end: DateTime, category: String, description: String): Long?

  @SqlUpdate("UPDATE Punch SET start = :start, end = :end, category = :category, description = :description WHERE id = :id")
  fun update(id: Long?, start: DateTime, end: DateTime, category: String, description: String)

  @SqlQuery("SELECT * FROM Punch WHERE start >= :start and end <= :end")
  fun listForDates(start: DateTime, end: DateTime): List<Punch>

  @SqlQuery("SELECT COUNT(id) > 0 FROM Punch WHERE start >= :start and end <= :end")
  fun anyInRange(start: DateTime, end: DateTime): Boolean?

  @SqlQuery("SELECT * FROM Punch WHERE id = :id")
  fun findById(id: Long?): Punch
}
