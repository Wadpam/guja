package com.wadpam.guja.filter;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;
import java.util.Collection;


/**
 * Wrap response with content type x-protobuf in a container containing the response code.
 * @author mattiaslevin
 */
@Provider
@Singleton
public class ProtoWrapperFilter implements ContainerResponseFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProtoWrapperFilter.class);

    public static final String APPLICATION_X_PROTOBUF = "application/x-protobuf";
    public static final MediaType APPLICATION_X_PROTOBUF_TYPE = new MediaType("application", "x-protobuf");


    @Override
    public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {

        Collection<Object> contentTypes = response.getHttpHeaders().get("Content-Type");
        //LOGGER.debug("content types {}", contentTypes);
        if (null != contentTypes) {

            for (Object contentType : contentTypes) {
                if (contentType.equals(APPLICATION_X_PROTOBUF_TYPE)) {
                    LOGGER.debug("Content type is x-protobuf, wrap response entity");

                    ResponseCodeEntityWrapper<Object> wrapper = new ResponseCodeEntityWrapper<>(response.getStatus(), response.getEntity());
                    response.setEntity(wrapper, response.getEntityType());

                    break;
                }
            }

        }

        return response;

    }

}
