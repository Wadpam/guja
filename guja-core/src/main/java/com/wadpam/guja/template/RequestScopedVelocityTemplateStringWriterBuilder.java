package com.wadpam.guja.template;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.servlet.RequestScoped;
import com.wadpam.guja.oauth2.provider.RequestScopedLocale;
import org.apache.velocity.VelocityContext;

/**
 * A request scoped velocity template builder bound to the locale of the current request.
 *
 * @author mattias
 */
@RequestScoped
public class RequestScopedVelocityTemplateStringWriterBuilder extends VelocityTemplateStringWriterBuilder {

  @Inject
  public RequestScopedVelocityTemplateStringWriterBuilder(Provider<RequestScopedLocale> localeProvider) {
    super(null, localeProvider.get().getLocale(), new VelocityContext());
  }

}
