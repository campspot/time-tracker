package com.campspot.resources

import com.campspot.api.Punch
import com.campspot.lib.PunchLib
import com.codahale.metrics.annotation.Timed
import io.dropwizard.jersey.params.DateTimeParam
import org.hibernate.validator.valuehandling.UnwrapValidatedValue
import org.skife.jdbi.v2.TransactionIsolationLevel
import org.skife.jdbi.v2.sqlobject.Transaction
import javax.validation.Valid
import javax.validation.constraints.NotNull
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/punches")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class PunchResource(private val punchLib: PunchLib) {

  @POST
  @Timed
  @Valid
  @Transaction(TransactionIsolationLevel.READ_COMMITTED)
  fun create(@Valid @NotNull punch: Punch): Punch {
    return punchLib.create(punch)
  }

  @PUT
  @Timed
  @Valid
  @Transaction(TransactionIsolationLevel.READ_COMMITTED)
  fun update(@Valid @NotNull punch: Punch): Punch {
    return punchLib.update(punch)
  }

  @GET
  @Timed
  @Valid
  @Transaction(TransactionIsolationLevel.READ_COMMITTED)
  fun list(
    @NotNull @UnwrapValidatedValue @QueryParam("start") start: DateTimeParam,
    @NotNull @UnwrapValidatedValue @QueryParam("end") end: DateTimeParam
  ): List<Punch> {
    return punchLib.listForDates(start.get(), end.get())
  }
}
