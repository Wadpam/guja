package com.wadpam.guja.guice;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.wadpam.guja.admintask.AdminTask;
import com.wadpam.guja.api.*;
import com.wadpam.guja.i18n.*;
import com.wadpam.guja.readerwriter.ResponseCodeProtoMessageBodyWriter;
import com.wadpam.guja.service.EmailService;
import com.wadpam.guja.service.EmailServiceImpl;


/**
 * Configure Guice module.
 *
 * @author mattiaslevin
 */
public class GujaBaseModule extends AbstractModule {


  @Override
  protected void configure() {

    bind(MonitorResource.class);

    bind(DiagnosticsResource.class);
    bind(DiagnosticsResource.DiagnosticsLogger.class).to(DiagnosticsResource.DefaultDiagnosticLogger.class);

    bind(VersionCheckResource.class);
    bind(VersionCheckResource.VersionCheckPredicate.class).to(SemanticVersionCheckPredicate.class);

    bind(Localization.class).annotatedWith(Dynamic.class).toProvider(DynamicLocalizationProvider.class);
    bind(Localization.class).annotatedWith(PropertyFile.class).toProvider(PropertyFileLocalizationProvider.class);

    bind(GAEBlobResource.class);

    bind(EmailService.class).to(EmailServiceImpl.class);

    bind(ResponseCodeProtoMessageBodyWriter.class);

  }

}
