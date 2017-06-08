package com.campspot.exceptions

import javax.ws.rs.WebApplicationException

class PunchCannotOverlapException : WebApplicationException("Punches cannot overlap", 422)
