package com.wadpam.guja.config;

/*
 * #%L
 * guja-base
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

import com.google.inject.AbstractModule;
import com.wadpam.guja.api.*;
import com.wadpam.guja.readerwriter.DiagnosticsProtoMessageBodyReader;
import com.wadpam.guja.readerwriter.ResponseCodeProtoMessageBodyWriter;
import com.wadpam.guja.readerwriter.VersionCheckProtoMessageBodyWriter;


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

    bind(ResponseCodeProtoMessageBodyWriter.class);
    bind(VersionCheckProtoMessageBodyWriter.class);
    bind(DiagnosticsProtoMessageBodyReader.class);

  }

}
