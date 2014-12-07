package com.wadpam.guja.config;


import com.google.common.collect.ImmutableMap;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import com.wadpam.guja.guice.GujaBaseModule;
import com.wadpam.guja.guice.GujaCoreModule;
import com.wadpam.guja.oauth2.web.OAuth2Filter;
import com.wadpam.guja.oauth2.web.Oauth2ClientAuthenticationFilter;
import net.sf.mardao.dao.DatastoreSupplier;
import net.sf.mardao.dao.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * Configure Guice modules and the web context.
 * @author mattiaslevin
 */
public class GujaGuiceServletContextListener extends GuiceServletContextListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(GujaGuiceServletContextListener.class);

    private static final String APP_CONFIG_PROPERTY_FILE ="/WEB-INF/app.properties";

    @Override
    protected Injector getInjector() {

        return Guice.createInjector(
                new GujaCoreModule(),
                new GujaBaseModule(),
                new JerseyServletModule() {

                    private void bindProperties() {
                        LOGGER.info("Bind application properties");

                        try {
                            Properties properties = new Properties();
                            properties.load(getServletContext().getResourceAsStream(APP_CONFIG_PROPERTY_FILE));
                            Names.bindProperties(binder(), properties);
                        } catch (IOException e) {
                            LOGGER.error("Failed to load app properties from resource file {} with error {}", APP_CONFIG_PROPERTY_FILE, e);
                        }

                    }

                    @Override
                    protected void configureServlets() {

                        // Bindings
                        bindProperties();

                        bind(Supplier.class).to(DatastoreSupplier.class);

                        // Filters
                        //filter("/*").through(PersistFilter.class);
                        filter("/api/*").through(OAuth2Filter.class);
                        filter("/oauth/*").through(Oauth2ClientAuthenticationFilter.class);

                        // Servlets
                        serve("/*").with(GuiceContainer.class, ImmutableMap.of(
                                "jersey.config.server.tracing.type", "ALL",
                                "com.sun.jersey.spi.container.ContainerResponseFilters", "com.wadpam.guja.filter.ProtoWrapperFilter",
                                "com.sun.jersey.spi.container.ResourceFilters", "com.sun.jersey.api.container.filter.RolesAllowedResourceFilterFactory"
                        ));

                        // TODO Find a better way to configure Jersey filters (Guice integration does not support Jersey filter configuration here)
                    }
                }
        );
    }
}



