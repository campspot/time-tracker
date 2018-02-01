package com.campspot.api

import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.dropwizard.jackson.Jackson
import io.dropwizard.testing.FixtureHelpers.fixture
import org.assertj.core.api.Assertions.assertThat
import org.joda.time.DateTimeZone
import javax.validation.Validation

object APIObjectTester {
  private val MAPPER = Jackson.newObjectMapper()
  private val validator = Validation.buildDefaultValidatorFactory().validator

  init {
    MAPPER.registerModule(KotlinModule())
    MAPPER.configure(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
    MAPPER.setTimeZone(DateTimeZone.UTC.toTimeZone())
  }

  fun <T : Any> testSerialization(fromValue: T, fixturePath: String) {
    val expected = MAPPER.writeValueAsString(MAPPER.readValue(fixture(fixturePath), fromValue.javaClass))

    assertThat(MAPPER.writeValueAsString(fromValue)).isEqualTo(expected)

    val constraintViolations = validator.validate(fromValue)

    assertThat(constraintViolations).isEmpty()
  }

  fun <T : Any> testDeserialization(expectedValue: T, fixturePath: String) {
    val actual = MAPPER.readValue(fixture(fixturePath), expectedValue.javaClass)
    assertThat(actual).isEqualToComparingFieldByField(expectedValue)

    val constraintViolations = validator.validate(actual)

    assertThat(constraintViolations).isEmpty()
  }
}



