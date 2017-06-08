package com.campspot.exceptions

import javax.ws.rs.WebApplicationException

class EntityNotFoundException(message: String) : WebApplicationException(message, 404)
