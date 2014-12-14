package com.wadpam.guja.guice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.Transactional;
import com.google.inject.persist.UnitOfWork;
import com.wadpam.guja.admintask.AdminTask;
import com.wadpam.guja.exceptions.RestExceptionMapper;
import com.wadpam.guja.oauth2.api.*;
import com.wadpam.guja.oauth2.dao.DConnectionDaoBean;
import com.wadpam.guja.oauth2.dao.DFactoryDaoBean;
import com.wadpam.guja.oauth2.dao.DOAuth2UserDaoBean;
import com.wadpam.guja.oauth2.dao.DUserDaoBean;
import com.wadpam.guja.oauth2.providers.*;
import com.wadpam.guja.oauth2.service.UserAdminTask;
import com.wadpam.guja.oauth2.service.UserService;
import com.wadpam.guja.oauth2.service.UserServiceImpl;
import com.wadpam.guja.provider.NonNullObjectMapperProvider;
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

    bind(NonNullObjectMapperProvider.class);
    bind(ObjectMapper.class).toProvider(NonNullObjectMapperProvider.class);

    bind(PasswordEncoder.class).to(DefaultPasswordEncoder.class);
    bind(AccessTokenGenerator.class).to(DefaultAccessTokenGenerator.class);
    bind(ServerEnvironmentProvider.class);

    bind(UserAdminTask.class);

    bind(OAuth2Resource.class);

    bind(UserAuthenticationProvider.class).to(UserServiceImpl.class);
    bind(Oauth2UserProvider.class).to(UserServiceImpl.class);

    bind(ConnectionResource.class);
    bind(DConnectionDaoBean.class);

    bind(FactoryResource.class);
    bind(DFactoryDaoBean.class);

    bind(UserResource.class);
    bind(UserService.class).to(UserServiceImpl.class);
    bind(DUserDaoBean.class);

    bind(OAuth2UserResource.class);
    bind(DOAuth2UserDaoBean.class);

    Multibinder<AdminTask> adminTaskBinder = Multibinder.newSetBinder(binder(), AdminTask.class);
    adminTaskBinder.addBinding().to(UserAdminTask.class);

    MardaoTransactionManager transactionManager = new MardaoTransactionManager(getProvider(Supplier.class));
    requestInjection(transactionManager);
    bindInterceptor(Matchers.any(), Matchers.annotatedWith(Transactional.class), transactionManager);

  }
}
