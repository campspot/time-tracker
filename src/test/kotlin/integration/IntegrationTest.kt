package integration

import com.campspot.TimeTrackingApplication
import com.campspot.client.EntityValidator
import com.campspot.client.TimeTrackingClient
import io.dropwizard.client.JerseyClientBuilder
import io.dropwizard.jdbi.DBIFactory
import io.dropwizard.testing.junit.DropwizardAppRule
import liquibase.Liquibase
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor
import org.joda.time.DateTimeZone
import org.junit.Before
import org.junit.BeforeClass
import org.junit.ClassRule
import org.skife.jdbi.v2.DBI
import uy.klutter.db.jdbi.v2.attachKotlinPlugin
import java.util.*
import javax.ws.rs.client.Client

open class IntegrationTest {
  companion object {
    @ClassRule @JvmField val RULE = DropwizardAppRule(TimeTrackingApplication::class.java, "test.yml")
    var testClient: Client? = null
    var apiClient: TimeTrackingClient? = null

    var jdbi: DBI? = null

    @BeforeClass
    @JvmStatic
    fun setupTests() {
      val utc = DateTimeZone.UTC.toTimeZone()

      testClient = JerseyClientBuilder(RULE.environment)
        .using(RULE.configuration.jerseyClientConfiguration)
        .build("test client")
      apiClient = TimeTrackingClient("http", "localhost", RULE.localPort, testClient!!, EntityValidator(RULE.environment.validator))

      val factory = object : DBIFactory() {
        override fun databaseTimeZone(): Optional<TimeZone> {
          return Optional.of(utc)
        }
      }
      jdbi = factory.build(RULE.environment, RULE.configuration.dataSourceFactory, "db")
      jdbi?.attachKotlinPlugin()

      val ds = RULE.configuration.dataSourceFactory.build(RULE.environment.metrics(), "migrations")
      ds.connection.use({ connection ->
        val migrator = Liquibase("migrations.xml", ClassLoaderResourceAccessor(), JdbcConnection(connection))
        migrator.update("")
      })
    }
  }

  @Before
  fun setup() {
    val handle = jdbi!!.open()
    val tables = handle.createQuery(
      "SELECT TABLE_NAME " +
        "FROM INFORMATION_SCHEMA.TABLES " +
        "WHERE TABLE_SCHEMA = 'time_tracking_test' " +
        "AND TABLE_TYPE = 'BASE TABLE' " +
        "AND TABLE_NAME NOT LIKE '%DATABASECHANGELOG%';"
    ).mapTo(String::class.java).list()

    tables.forEach { t -> handle.execute("TRUNCATE TABLE $t") }
  }
}
