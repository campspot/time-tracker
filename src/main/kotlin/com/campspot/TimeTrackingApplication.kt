package com.campspot

import com.campspot.dao.PunchDAO
import com.campspot.lib.MockableObject
import com.campspot.lib.PunchLib
import com.campspot.middleware.CharsetResponseFilter
import com.campspot.resources.PunchResource
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.github.arteam.jdbi3.JdbiFactory
import io.dropwizard.Application
import io.dropwizard.assets.AssetsBundle
import io.dropwizard.db.PooledDataSourceFactory
import io.dropwizard.migrations.MigrationsBundle
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.sqlobject.kotlin.KotlinSqlObjectPlugin
import org.joda.time.DateTimeZone
import java.util.*


class TimeTrackingApplication : Application<TimeTrackingConfiguration>() {

  val assetsBundle = AssetsBundle("/assets", "/", "index.html")

  val migrationsBundle = object : MigrationsBundle<TimeTrackingConfiguration>() {
    override fun getDataSourceFactory(configuration: TimeTrackingConfiguration): PooledDataSourceFactory {
      return configuration.dataSourceFactory
    }
  }

  override fun getName(): String {
    return "TimeTracking"
  }

  override fun initialize(bootstrap: Bootstrap<TimeTrackingConfiguration>) {
    bootstrap.addBundle(assetsBundle)
    bootstrap.addBundle(migrationsBundle)
  }

  override fun run(configuration: TimeTrackingConfiguration, environment: Environment) {
    val utc = DateTimeZone.UTC.toTimeZone()

    val objectMapper = environment.objectMapper
    objectMapper.registerModule(KotlinModule())
    objectMapper.configure(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
    objectMapper.setTimeZone(utc)

    val factory = JdbiFactory()
    val jdbi = factory.build(environment, configuration.dataSourceFactory, "db")
    jdbi.installPlugin(KotlinPlugin())
    jdbi.installPlugin(KotlinSqlObjectPlugin())
    TimeZone.setDefault(DateTimeZone.UTC.toTimeZone())

    environment.jersey().register(CharsetResponseFilter())

    val punchDAO = jdbi.onDemand<PunchDAO>(PunchDAO::class.java)

    val punchLib = PunchLib(punchDAO, MockableObject())

    environment.jersey().register(PunchResource(punchLib))
  }

  companion object {
    @Throws(Exception::class)
    @JvmStatic fun main(args: Array<String>) {
      TimeTrackingApplication().run(*args)
    }
  }
}
