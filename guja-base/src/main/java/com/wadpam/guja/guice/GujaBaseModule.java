package com.wadpam.guja.guice;

import com.google.inject.multibindings.Multibinder;
import com.wadpam.guja.api.DiagnosticsResource;
import com.wadpam.guja.api.MonitorResource;
import com.google.inject.AbstractModule;
import com.wadpam.guja.api.VersionCheckResource;
import com.wadpam.guja.i18n.*;
import com.wadpam.guja.readerwriter.ResponseCodeProtoMessageBodyWriter;


/**
 * Configure Guice module.
 * @author mattiaslevin
 */
public class GujaBaseModule extends AbstractModule {


    @Override
    protected void configure() {

        bind(MonitorResource.class);

        bind(DiagnosticsResource.class);
        bind(DiagnosticsResource.DiagnosticsLogger.class).to(DiagnosticsResource.DefaultDiagnosticLogger.class);

        // Add predicates in the project module. Adding binders across modules will add (not replace) additional binders.
        Multibinder<VersionCheckResource.VersionCheckPredicate> versionPredicateBinder =
                Multibinder.newSetBinder(binder(), VersionCheckResource.VersionCheckPredicate.class);
        bind(VersionCheckResource.class);

        bind(Localization.class).annotatedWith(Dynamic.class).toProvider(DynamicLocalizationProvider.class);
        bind(Localization.class).annotatedWith(PropertyFile.class).toProvider(PropertyFileLocalizationProvider.class);

        bind(ResponseCodeProtoMessageBodyWriter.class);

    }

}
