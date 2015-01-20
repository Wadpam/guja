package com.wadpam.guja.config;

/*
 * #%L
 * guja-be-war
 * %%
 * Copyright (C) 2014 Wadpam
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.google.common.collect.ImmutableMap;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.matcher.Matchers;
import com.google.inject.name.Names;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import com.wadpam.guja.cache.CacheBuilder;
import com.wadpam.guja.cache.CacheMethodInterceptor;
import com.wadpam.guja.oauth2.web.OAuth2Filter;
import com.wadpam.guja.oauth2.web.Oauth2ClientAuthenticationFilter;
import net.sf.mardao.dao.Cached;
import net.sf.mardao.dao.DatastoreSupplier;
import net.sf.mardao.dao.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * Configure Guice modules and the web context.
 *
 * @author mattiaslevin
 */
public class GujaGuiceServletContextListener extends GuiceServletContextListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(GujaGuiceServletContextListener.class);

    private static final String APP_CONFIG_PROPERTY_FILE = "/WEB-INF/app.properties";

    @Override
    protected Injector getInjector() {

        return Guice.createInjector(
                // bind both authorization server and federated:
                new GujaCoreModule(true, true),
                new GujaBaseModule(),
                new GujaGAEModule(),
                new JerseyServletModule() {
                    private Properties bindProperties() {
                        LOGGER.info("Bind application properties");

                        Properties properties = new Properties();
                        try {
                            properties.load(getServletContext().getResourceAsStream(APP_CONFIG_PROPERTY_FILE));
                            Names.bindProperties(binder(), properties);
                        } catch (IOException e) {
                            LOGGER.error("Failed to load app properties from resource file {} with error {}", APP_CONFIG_PROPERTY_FILE, e);
                        }
                        return properties;
                    }

                    @Override
                    protected void configureServlets() {

                        // Bindings
                        Properties props = bindProperties();
                        // Cached annotation
                        bindInterceptor(Matchers.annotatedWith(Cached.class),
                                Matchers.annotatedWith(Cached.class),
                                new CacheMethodInterceptor(getProvider(CacheBuilder.class)));

                        // Filters
                        //filter("/*").through(PersistFilter.class);
                        filter("/api/*").through(OAuth2Filter.class);
                        filter("/oauth/authorize", "/oauth/refresh", "/oauth/revoke").through(Oauth2ClientAuthenticationFilter.class);

                        bind(Supplier.class).to(DatastoreSupplier.class);

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



