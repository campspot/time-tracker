package com.campspot.dao

import com.codahale.metrics.MetricRegistry
import io.dropwizard.db.DataSourceFactory
import io.dropwizard.jackson.Jackson
import io.dropwizard.jdbi.DBIFactory
import io.dropwizard.setup.Environment
import liquibase.Liquibase
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor
import org.joda.time.DateTimeZone
import org.junit.BeforeClass
import org.skife.jdbi.v2.DBI
import uy.klutter.db.jdbi.v2.attachKotlinPlugin
import java.util.*


open class DAOTest {
  companion object {
    var jdbi: DBI? = null

    @BeforeClass
    @JvmStatic
    fun setupTests() {
      val env = Environment("test-env", Jackson.newObjectMapper(), null, MetricRegistry(), null)
      val dataSourceFactory = DataSourceFactory()
      dataSourceFactory.driverClass = "org.h2.Driver"
      dataSourceFactory.url = "jdbc:h2:mem:testDb"
      dataSourceFactory.user = "sa"
      dataSourceFactory.password = ""

      val utc = DateTimeZone.UTC.toTimeZone()
      val factory = object : DBIFactory() {
        override fun databaseTimeZone(): Optional<TimeZone> {
          return Optional.of(utc)
        }
      }
      jdbi = factory.build(env, dataSourceFactory, "db")
      jdbi?.attachKotlinPlugin()

      val ds = dataSourceFactory.build(env.metrics(), "migrations")
      ds.connection.use({ connection ->
        val migrator = Liquibase("migrations.xml", ClassLoaderResourceAccessor(), JdbcConnection(connection))
        migrator.update("")
      })
    }
  }
}
