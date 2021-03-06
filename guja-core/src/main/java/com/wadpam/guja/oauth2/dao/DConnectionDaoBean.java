package com.wadpam.guja.oauth2.dao;

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


import com.google.inject.Inject;
import com.wadpam.guja.exceptions.InternalServerErrorRestException;
import com.wadpam.guja.oauth2.domain.DConnection;
import net.sf.mardao.core.CacheConfig;
import net.sf.mardao.dao.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.cache.annotation.*;
import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Implementation of Business Methods related to entity DConnection.
 * This (empty) class is generated by mardao, but edited by developers.
 * It is not overwritten by the generator once it exists.
 * <p/>
 * Generated on 2014-12-02T21:52:55.127+0100.
 *
 * @author mardao DAO generator (net.sf.mardao.plugin.ProcessDomainMojo)
 */
@CacheConfig(expiresAfterSeconds = 60 * 15) // 15 minutes cache
@CacheDefaults(cacheName = "DConnection")
public class DConnectionDaoBean extends GeneratedDConnectionDaoImpl {
  private static final Logger LOGGER = LoggerFactory.getLogger(DConnectionDaoBean.class);


  @Inject
  public DConnectionDaoBean(Supplier supplier) {
    super(supplier);
  }


  /**
   * Enable caching based on access token.
   * @param accessToken access token
   */
  @CacheResult
  @Override
  public DConnection findByAccessToken(@CacheKey String accessToken) {
    return super.findByAccessToken(accessToken);
  }

  /**
   * Remove a connection, both from database and cache.
   * Always use this method when deleting a connection.
   * @param accessToken connections unique access token. Can not be null.
   * @param id connection id
   */
  @CacheRemove
  public void deleteWithCacheKey(@CacheKey String accessToken, Long id) {
    try {
      delete(id);
    } catch (IOException e) {
      LOGGER.error("Failed to delete connection entity {}", e);
      throw new InternalServerErrorRestException("Failed to delete connection entity");
    }
  }


  /**
   * Invalidate the cache only.
   * @param accessToken cache key to invalidate
   */
  @CacheRemove
  public void invalidateCacheKey(@CacheKey String accessToken) {
    // Do nothing
  }

  /**
   * Create and update a connection, both in database and cache.
   * Always use this method when creating or updating a connection.
   * @param accessToken unique access token. Can not be null.
   * @param connection connection object
   * @return connection id
   */
  @CachePut
  public Long putWithCacheKey(@CacheKey String accessToken, @CacheValue DConnection connection) {
    //checkNotNull(accessToken); // Fail quick
    try {
      return put(connection);
    } catch (IOException e) {
      LOGGER.error("Failed to create/update connection entity {}", e);
      throw new InternalServerErrorRestException("Failed to create/update connection entity");
    }
  }

}
