package com.wadpam.guja.oauth2.provider;

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

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;
import com.google.inject.Singleton;

import java.security.SecureRandom;

/**
 * Generate random tokens based on md5 hashing and url safe Base64 encoding.
 *
 * @author mattiaslevin
 */
@Singleton
public class DefaultTokenGenerator implements TokenGenerator {

  private static final SecureRandom random = new SecureRandom();

  @Override
  public String generate() {

    byte[] randomBytes = new byte[24];
    random.nextBytes(randomBytes);

    HashCode hashCode = Hashing.goodFastHash(256).newHasher()
        .putBytes(randomBytes)
        .putLong(System.nanoTime())
        .hash();

    return BaseEncoding.base64Url().omitPadding().encode(hashCode.asBytes());
  }


}
