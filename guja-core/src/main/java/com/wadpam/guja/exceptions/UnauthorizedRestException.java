package com.wadpam.guja.exceptions;

import javax.ws.rs.core.Response;

/**
 * Unauthorized (401)
 * @author mattiaslevin
 */
public class UnauthorizedRestException  extends RestException {

    public UnauthorizedRestException() {
        super(Response.Status.UNAUTHORIZED);
    }

    public UnauthorizedRestException(String message) {
        super(Response.Status.UNAUTHORIZED, message);
    }

}
