package com.campspot.middleware.charset

import javax.ws.rs.container.ContainerRequestContext
import javax.ws.rs.container.ContainerResponseContext
import javax.ws.rs.container.ContainerResponseFilter
import javax.ws.rs.core.MediaType

class CharsetResponseFilter : ContainerResponseFilter {
  override fun filter(request: ContainerRequestContext, response: ContainerResponseContext) {
    val type = response.mediaType
    if (type != null) {
      var contentType = type.toString()
      if (contentType.contains(MediaType.APPLICATION_JSON) && !contentType.contains("charset")) {
        contentType += "; charset=utf-8"
        response.headers.putSingle("Content-Type", contentType)
      }
    }
  }
}
