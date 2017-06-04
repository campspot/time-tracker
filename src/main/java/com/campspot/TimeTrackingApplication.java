package com.campspot;

import com.campspot.dao.PunchDAO;
import com.campspot.lib.PunchLib;
import com.campspot.resources.PunchResource;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.skife.jdbi.v2.DBI;

public class TimeTrackingApplication extends Application<TimeTrackingConfiguration> {
  private static final AssetsBundle assetsBundle = new AssetsBundle("/assets", "/", "index.html");

  private static final MigrationsBundle<TimeTrackingConfiguration> migrationsBundle = new MigrationsBundle<TimeTrackingConfiguration>() {
    @Override
    public PooledDataSourceFactory getDataSourceFactory(TimeTrackingConfiguration configuration) {
      return configuration.getDataSourceFactory();
    }
  };

  public static void main(final String[] args) throws Exception {
    new TimeTrackingApplication().run(args);
  }

  @Override
  public String getName() {
    return "TimeTracking";
  }

  @Override
  public void initialize(final Bootstrap<TimeTrackingConfiguration> bootstrap) {
    bootstrap.addBundle(assetsBundle);
    bootstrap.addBundle(migrationsBundle);
  }

  @Override
  public void run(final TimeTrackingConfiguration configuration,
                  final Environment environment) {

    ObjectMapper objectMapper = environment.getObjectMapper();
    objectMapper.registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
    objectMapper.configure(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    final DBIFactory factory = new DBIFactory();
    final DBI jdbi = factory.build(environment, configuration.getDataSourceFactory(), "db");

    PunchDAO punchDAO = jdbi.onDemand(PunchDAO.class);

    PunchLib punchLib = new PunchLib(punchDAO);

    environment.jersey().register(new PunchResource(punchLib));
  }
}
