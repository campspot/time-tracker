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
import io.dropwizard.hibernate.ScanningHibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class TimeTrackingApplication extends Application<TimeTrackingConfiguration> {
  private static final AssetsBundle assetsBundle = new AssetsBundle("/assets", "/", "index.html");

  private static final MigrationsBundle<TimeTrackingConfiguration> migrationsBundle = new MigrationsBundle<TimeTrackingConfiguration>() {
    @Override
    public PooledDataSourceFactory getDataSourceFactory(TimeTrackingConfiguration configuration) {
      return configuration.getDataSourceFactory();
    }
  };

  private static final ScanningHibernateBundle<TimeTrackingConfiguration> hibernateBundle = new ScanningHibernateBundle<TimeTrackingConfiguration>("com.campspot.dao.entities") {
    @Override
    protected void configure(Configuration configuration) {
      configuration.addPackage("com.campspot.dao.entities");
      super.configure(configuration);
    }

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
    bootstrap.addBundle(hibernateBundle);
  }

  @Override
  public void run(final TimeTrackingConfiguration configuration,
                  final Environment environment) {

    ObjectMapper objectMapper = environment.getObjectMapper();
    objectMapper.registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
    objectMapper.configure(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    SessionFactory sessionFactory = hibernateBundle.getSessionFactory();
    PunchDAO punchDAO = new PunchDAO(sessionFactory);

    PunchLib punchLib = new PunchLib(punchDAO);

    environment.jersey().register(new PunchResource(punchLib));
  }
}
