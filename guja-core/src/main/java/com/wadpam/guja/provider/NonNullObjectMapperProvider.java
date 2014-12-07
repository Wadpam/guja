package com.wadpam.guja.provider;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

/**
 * Custom Jackson ObjectMapper.
 * @author mattiaslevin
 */
@Provider
@Singleton
public class NonNullObjectMapperProvider implements ContextResolver<ObjectMapper>, com.google.inject.Provider<ObjectMapper> {
    private static final Logger LOGGER = LoggerFactory.getLogger(NonNullObjectMapperProvider.class);

    final private ObjectMapper defaultObjectMapper;

    public NonNullObjectMapperProvider() {
        defaultObjectMapper = createDefaultMapper();
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        LOGGER.debug("Get ObjectMapper Jersey context");
        return defaultObjectMapper;
    }

    private static ObjectMapper createDefaultMapper() {
        final ObjectMapper result = new ObjectMapper();

        result.configure(SerializationFeature.INDENT_OUTPUT, true);

        // Ignore unknown fields during deserialization
        result.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // Do not serialize null values
        result.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        return result;
    }

    // Guice methods

    @Override
    public ObjectMapper get() {
        LOGGER.debug("Get ObjectMapper Guice context");
        return defaultObjectMapper;
    }

}