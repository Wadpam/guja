#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import com.wadpam.guja.cache.annotations.CacheAnnotationsModule;
import com.wadpam.guja.config.GujaBaseModule;
import com.wadpam.guja.config.GujaContactModule;
import com.wadpam.guja.config.GujaCoreModule;
import com.wadpam.guja.config.GujaGaeModule;
import com.wadpam.guja.jackson.NonNullObjectMapperProvider;
import com.wadpam.guja.oauth2.web.OAuth2Filter;
import com.wadpam.guja.oauth2.web.Oauth2ClientAuthenticationFilter;
import com.wadpam.guja.persist.MardaoDatastoreModule;
import com.wadpam.guja.web.CORSFilter;
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
        new GujaGaeModule(),
        new GujaContactModule(),
        new MardaoDatastoreModule(),
        new CacheAnnotationsModule(),
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

          private void bindDaos() {
            LOGGER.info("Bind daos");

            bind(Supplier.class).to(DatastoreSupplier.class);

            //TODO: Bind your daos here
          }

          private void bindResources() {
            LOGGER.info("Bind resources");

            //TODO: Bind your resources here
          }

          @Override
          protected void configureServlets() {

            // Bindings
            Properties props = bindProperties();

            bindDaos();
            bindResources();

            bind(NonNullObjectMapperProvider.class);
            bind(ObjectMapper.class).toProvider(NonNullObjectMapperProvider.class);

            // Filters
            //filter("/*").through(PersistFilter.class);

            // Enable CORS if running on dev server
            filter("/*").through(CORSFilter.class);
            //filter("/*").through(CORSFilter.class, ImmutableMap.of("alwaysEnabled", "true"));

            filter("/oauth/authorize", "/oauth/refresh", "/oauth/revoke").through(Oauth2ClientAuthenticationFilter.class);

            filter("/api/*").through(OAuth2Filter.class);

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



