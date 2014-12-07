package com.wadpam.guja.exceptions;

import com.google.appengine.repackaged.com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Map rest exception to a http response code and entity.
 * @author mattiaslevin
 */
@Provider
@Singleton
public class RestExceptionMapper implements ExceptionMapper<RestException> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestExceptionMapper.class);

    @Context
    HttpHeaders httpHeaders;

    @Override
    public Response toResponse(RestException exception) {
        //LOGGER.debug("Map exception - {}", exception.getMessage());
        //LOGGER.debug("Header context {}", httpHeaders.getAcceptableMediaTypes());

        ImmutableMap.Builder<String, String> errorMessage = new ImmutableMap.Builder<>();
        if (null != exception.getJson()) {
            errorMessage.putAll(exception.getJson());
        }

        if (null != exception.getMessage()) {
            errorMessage.put("message", exception.getMessage());
        }

        errorMessage.put("responseCode", String.valueOf(exception.getStatus().getStatusCode()));

        return Response.status(exception.getStatus())
                .entity(errorMessage.build())
                .type(httpHeaders.getAcceptableMediaTypes().get(0))
                .build();
    }

}
