package com.campspot;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class TimeTrackingApplication extends Application<TimeTrackingConfiguration> {

  public static void main(final String[] args) throws Exception {
    new TimeTrackingApplication().run(args);
  }

  @Override
  public String getName() {
    return "TimeTracking";
  }

  @Override
  public void initialize(final Bootstrap<TimeTrackingConfiguration> bootstrap) {
    bootstrap.addBundle(new AssetsBundle("/assets", "/", "index.html"));
  }

  @Override
  public void run(final TimeTrackingConfiguration configuration,
                  final Environment environment) {
  }
}
