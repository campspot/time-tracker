package com.campspot.resources;

import com.campspot.api.Punch;
import com.campspot.lib.PunchLib;
import com.codahale.metrics.annotation.Timed;
import io.dropwizard.jersey.params.DateTimeParam;
import org.hibernate.validator.valuehandling.UnwrapValidatedValue;
import org.skife.jdbi.v2.TransactionIsolationLevel;
import org.skife.jdbi.v2.sqlobject.Transaction;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/punches")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PunchResource {
  private final PunchLib punchLib;

  public PunchResource(PunchLib punchLib) {
    this.punchLib = punchLib;
  }

  @POST
  @Timed
  @Valid
  @Transaction(TransactionIsolationLevel.READ_COMMITTED)
  public Punch create(@Valid @NotNull Punch punch) {
    return punchLib.create(punch);
  }

  @PUT
  @Timed
  @Valid
  @Transaction(TransactionIsolationLevel.READ_COMMITTED)
  public Punch update(@Valid @NotNull Punch punch) {
    return punchLib.update(punch);
  }

  @GET
  @Timed
  @Valid
  @Transaction(TransactionIsolationLevel.READ_COMMITTED)
  public List<Punch> list(
    @NotNull @UnwrapValidatedValue @QueryParam("start") DateTimeParam start,
    @NotNull @UnwrapValidatedValue @QueryParam("end") DateTimeParam end
  ) {
    return punchLib.listForDates(start.get(), end.get());
  }
}
