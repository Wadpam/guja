package com.wadpam.guja.filter;

/**
 * Wrap an entity response object with the http response code.
 * @author mattiaslevin
 */
public class ResponseCodeEntityWrapper<T> {

    private int responseCode;

    private T entity;

    public ResponseCodeEntityWrapper(int responseCode, T entity) {
        this.responseCode = responseCode;
        this.entity = entity;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public T getEntity() {
        return entity;
    }
}
