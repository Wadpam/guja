package com.wadpam.guja.oauth2.providers;


import com.google.appengine.repackaged.com.google.common.io.BaseEncoding;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;

import java.security.SecureRandom;

/**
 * Generate access tokens based on md5 hashing
 * @author mattiaslevin
 */
public class DefaultAccessTokenGenerator implements AccessTokenGenerator {

    private static final SecureRandom random = new SecureRandom();

    @Override
    public String generate() {

        byte[] randomBytes = new byte[24];
        random.nextBytes(randomBytes);

        HashCode hashCode = Hashing.goodFastHash(256).newHasher()
                .putBytes(randomBytes)
                .putLong(System.nanoTime())
                .hash();

        return BaseEncoding.base64().encode(hashCode.asBytes());
    }


}
