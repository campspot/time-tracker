package com.campspot

import com.campspot.dao.PunchDAO
import com.campspot.lib.PunchLib
import com.campspot.resources.PunchResource
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.dropwizard.Application
import io.dropwizard.assets.AssetsBundle
import io.dropwizard.db.PooledDataSourceFactory
import io.dropwizard.jdbi.DBIFactory
import io.dropwizard.migrations.MigrationsBundle
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment

class TimeTrackingApplication : Application<TimeTrackingConfiguration>() {

  override fun getName(): String {
    return "TimeTracking"
  }

  override fun initialize(bootstrap: Bootstrap<TimeTrackingConfiguration>) {
    bootstrap.addBundle(assetsBundle)
    bootstrap.addBundle(migrationsBundle)
  }

  override fun run(configuration: TimeTrackingConfiguration, environment: Environment) {
    val objectMapper = environment.objectMapper
    objectMapper.registerModule(KotlinModule())
    objectMapper.configure(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

    val factory = DBIFactory()
    val jdbi = factory.build(environment, configuration.dataSourceFactory, "db")

    val punchDAO = jdbi.onDemand<PunchDAO>(PunchDAO::class.java)

    val punchLib = PunchLib(punchDAO)

    environment.jersey().register(PunchResource(punchLib))
  }

  companion object {
    private val assetsBundle = AssetsBundle("/assets", "/", "index.html")

    private val migrationsBundle = object : MigrationsBundle<TimeTrackingConfiguration>() {
      override fun getDataSourceFactory(configuration: TimeTrackingConfiguration): PooledDataSourceFactory {
        return configuration.dataSourceFactory
      }
    }

    @Throws(Exception::class)
    @JvmStatic fun main(args: Array<String>) {
      TimeTrackingApplication().run(*args)
    }
  }
}
