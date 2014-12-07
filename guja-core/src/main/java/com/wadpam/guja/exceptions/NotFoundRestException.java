package com.wadpam.guja.exceptions;

import javax.ws.rs.core.Response;

/**
 * Map not found exception to response code 404
 * @author mattiaslevin
 */
public class NotFoundRestException extends RestException {

    public NotFoundRestException() {
        super(Response.Status.NOT_FOUND);
    }

    public NotFoundRestException(String message) {
        super(Response.Status.NOT_FOUND, message);
    }

}
