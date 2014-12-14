package com.wadpam.guja.filter;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.ElementType.TYPE;

/**
 * Annotate a protobuffer response entity that should not be wrapped in a proto wrapper.
 * @author mattiaslevin
 */
@Target({ TYPE })
@Retention(RUNTIME)
public @interface SkipProtoWrapper {}