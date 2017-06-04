package com.campspot

import com.fasterxml.jackson.annotation.JsonProperty
import io.dropwizard.Configuration
import io.dropwizard.db.DataSourceFactory

import javax.validation.Valid
import javax.validation.constraints.NotNull

class TimeTrackingConfiguration : Configuration() {
  @Valid
  @NotNull
  @JsonProperty("database")
  val dataSourceFactory = DataSourceFactory()
}
