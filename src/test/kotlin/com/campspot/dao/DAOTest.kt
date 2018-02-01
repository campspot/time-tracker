package com.campspot.dao

import com.codahale.metrics.MetricRegistry
import com.github.arteam.jdbi3.JdbiFactory
import io.dropwizard.db.DataSourceFactory
import io.dropwizard.jackson.Jackson
import io.dropwizard.setup.Environment
import liquibase.Liquibase
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.sqlobject.kotlin.KotlinSqlObjectPlugin
import org.junit.BeforeClass


open class DAOTest {
  companion object {
    var jdbi: Jdbi? = null

    @BeforeClass
    @JvmStatic
    fun setupTests() {
      val env = Environment("test-env", Jackson.newObjectMapper(), null, MetricRegistry(), null)
      val dataSourceFactory = DataSourceFactory()
      dataSourceFactory.driverClass = "org.h2.Driver"
      dataSourceFactory.url = "jdbc:h2:mem:testDb"
      dataSourceFactory.user = "sa"
      dataSourceFactory.password = ""

      val factory = JdbiFactory()
      jdbi = factory.build(env, dataSourceFactory, "db")
      jdbi!!.installPlugin(KotlinPlugin())
      jdbi!!.installPlugin(KotlinSqlObjectPlugin())

      val ds = dataSourceFactory.build(env.metrics(), "migrations")
      ds.connection.use({ connection ->
        val migrator = Liquibase("migrations.xml", ClassLoaderResourceAccessor(), JdbcConnection(connection))
        migrator.update("")
      })
    }
  }
}
