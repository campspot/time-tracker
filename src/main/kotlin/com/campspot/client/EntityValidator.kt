package com.campspot.client

import javax.validation.ConstraintViolationException
import javax.validation.Validator

class EntityValidator(private val validator: Validator) {
  fun <T> validate(entity: T?): T? {
    if (entity == null) {
      return entity
    }

    val constraintViolations = validator.validate(entity)

    if (!constraintViolations.isEmpty()) {
      val errors = constraintViolations
        .joinToString { it.toString() + "\n" }

      throw ConstraintViolationException(errors, constraintViolations)
    }

    return entity
  }
}
