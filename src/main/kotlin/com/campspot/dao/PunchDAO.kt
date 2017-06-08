package com.campspot.dao

import com.campspot.api.Punch
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys
import org.skife.jdbi.v2.sqlobject.SqlQuery
import org.skife.jdbi.v2.sqlobject.SqlUpdate
import java.time.ZonedDateTime

interface PunchDAO {
  @SqlUpdate("INSERT INTO Punch (start, end, category, description) VALUES (:punch.start, :punch.end, :punch.category, :punch.description)")
  @GetGeneratedKeys
  fun create(punch: Punch): Long

  @SqlUpdate("UPDATE Punch SET start = :punch.start, end = :punch.end, category = :punch.category, description = :punch.description WHERE id = :punch.id")
  fun update(punch: Punch)

  @SqlQuery("SELECT * FROM Punch WHERE start < :end AND end > :start")
  fun findAllInDateRange(start: ZonedDateTime, end: ZonedDateTime): List<Punch>

  @SqlQuery("SELECT COUNT(id) > 0 FROM Punch WHERE start < :end AND end > :start")
  fun anyInRange(start: ZonedDateTime, end: ZonedDateTime): Boolean

  @SqlQuery("SELECT * FROM Punch WHERE id = :id")
  fun findById(id: Long): Punch
}
