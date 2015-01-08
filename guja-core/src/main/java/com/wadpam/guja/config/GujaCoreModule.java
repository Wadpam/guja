package com.wadpam.guja.config;

/*
 * #%L
 * guja-core
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.Transactional;
import com.google.inject.persist.UnitOfWork;
import com.wadpam.guja.admintask.AdminTask;
import com.wadpam.guja.admintask.AdminTaskResource;
import com.wadpam.guja.exceptions.RestExceptionMapper;
import com.wadpam.guja.i18n.*;
import com.wadpam.guja.oauth2.api.*;
import com.wadpam.guja.oauth2.dao.DConnectionDaoBean;
import com.wadpam.guja.oauth2.dao.DFactoryDaoBean;
import com.wadpam.guja.oauth2.dao.DOAuth2UserDaoBean;
import com.wadpam.guja.oauth2.dao.DUserDaoBean;
import com.wadpam.guja.oauth2.provider.*;
import com.wadpam.guja.oauth2.service.UserAdminTask;
import com.wadpam.guja.oauth2.service.UserService;
import com.wadpam.guja.oauth2.service.UserServiceImpl;
import com.wadpam.guja.oauth2.web.Oauth2ClientAuthenticationFilter;
import com.wadpam.guja.provider.NonNullObjectMapperProvider;
import com.wadpam.guja.service.EmailService;
import com.wadpam.guja.service.JavaMailService;
import com.wadpam.guja.service.MockEmailService;
import com.wadpam.guja.template.RequestScopedVelocityTemplateStringWriterBuilder;
import net.sf.mardao.dao.Supplier;

/**
 * Binds {@link com.google.inject.persist.UnitOfWork}, {@link com.google.inject.persist.PersistService} and {@link com.wadpam.mardao.guice.MardaoTransactionManager}.
 *
 * @author osandstrom
 *         Date: 1/19/14 Time: 8:59 PM
 */
public class GujaCoreModule extends AbstractModule {

  @Override
  protected void configure() {

    bind(UnitOfWork.class).to(MardaoGuiceUnitOfWork.class);
    bind(PersistService.class).to(MardaoGuicePersistService.class);

    bind(RestExceptionMapper.class);

    bind(RequestScopedLocale.class);

    bind(Localization.class).annotatedWith(Dynamic.class).to(RequestScopedDynamicLocalization.class);
    bind(Localization.class).annotatedWith(PropertyFile.class).to(RequestScopedPropertyFileLocalization.class);

    bind(NonNullObjectMapperProvider.class);
    bind(ObjectMapper.class).toProvider(NonNullObjectMapperProvider.class);

    bind(PasswordEncoder.class).to(DefaultPasswordEncoder.class);
    bind(TokenGenerator.class).to(DefaultTokenGenerator.class);
    bind(TemporaryTokenCache.class);

    bind(OAuth2Resource.class);

    bind(UserAuthenticationProvider.class).to(UserServiceImpl.class);
    bind(Oauth2UserProvider.class).to(UserServiceImpl.class);

    bind(ConnectionResource.class);
    bind(DConnectionDaoBean.class);

    bind(FactoryResource.class);
    bind(DFactoryDaoBean.class);

    bind(RequestScopedVelocityTemplateStringWriterBuilder.class);

    bind(EmailService.class).to(JavaMailService.class);

    bind(UserResource.class);
    bind(UserService.class).to(UserServiceImpl.class);
    bind(DUserDaoBean.class);

    bind(OAuth2UserResource.class);
    bind(DOAuth2UserDaoBean.class);

    bind(AdminTaskResource.class);

    Multibinder<AdminTask> adminTaskBinder = Multibinder.newSetBinder(binder(), AdminTask.class);
    adminTaskBinder.addBinding().to(UserAdminTask.class);
    adminTaskBinder.addBinding().to(Oauth2ClientAuthenticationFilter.class);

    MardaoTransactionManager transactionManager = new MardaoTransactionManager(getProvider(Supplier.class));
    requestInjection(transactionManager);
    bindInterceptor(Matchers.any(), Matchers.annotatedWith(Transactional.class), transactionManager);

  }
}
