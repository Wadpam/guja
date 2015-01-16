package com.wadpam.guja.cache.annotations;

import com.google.appengine.repackaged.com.google.common.collect.Lists;
import com.google.appengine.repackaged.com.google.common.collect.Sets;
import com.wadpam.guja.util.Triplet;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.cache.annotation.*;
import java.lang.annotation.Annotation;
import java.lang.annotation.AnnotationFormatError;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Set;

/**
 * Cache invocation context containing information about invoked method.
 *
 * @author mattiaslevin
 */
public class GuiceCacheKeyInvocationContext<A extends Annotation> implements CacheKeyInvocationContext<A> {
  static final Logger LOGGER = LoggerFactory.getLogger(GuiceCacheKeyInvocationContext.class);

  private final MethodInvocation methodInvocation;
  private final Annotation methodAnnotation;
  private final String methodCacheName;

  private final CacheInvocationParameter[] allParameters;
  private final CacheInvocationParameter[] keyParameters;

  private final CacheInvocationParameter valueParameter;

  public enum CacheMethodType {
    CACHE_PUT, CACHE_RESULT, CACHE_REMOVE_ENTITY, CACHE_REMOVE_ALL
  }


  public GuiceCacheKeyInvocationContext(MethodInvocation methodInvocation,
                                        Annotation methodCacheAnnotation,
                                        String methodCacheName) {

    this.methodInvocation = methodInvocation;
    this.methodAnnotation = methodCacheAnnotation;
    this.methodCacheName = methodCacheName;

    Triplet<Collection<CacheInvocationParameter>, Collection<CacheInvocationParameter>, CacheInvocationParameter> invocationParams =
        getParameterDetails(methodInvocation);

    allParameters = invocationParams.first().toArray(new CacheInvocationParameter[invocationParams.first().size()]);
    keyParameters = invocationParams.second().toArray(new CacheInvocationParameter[invocationParams.second().size()]);
    valueParameter = invocationParams.third();

  }

  @Override
  public CacheInvocationParameter[] getKeyParameters() {
    return keyParameters;
  }

  @Override
  public CacheInvocationParameter getValueParameter() {
    return valueParameter;
  }

  @Override
  public Object getTarget() {
    return methodInvocation.getThis();
  }

  @Override
  public CacheInvocationParameter[] getAllParameters() {
    return allParameters;
  }

  @Override
  public <T> T unwrap(Class<T> cls) {
    if (cls.isAssignableFrom(MethodInvocation.class)) {
      return cls.cast(MethodInvocation.class);
    }
    throw new IllegalArgumentException("Unwapping to " + cls + " is not a supported by this implementation");
  }


  @Override
  public Method getMethod() {
    return methodInvocation.getMethod();
  }

  @Override
  public Set<Annotation> getAnnotations() {
    return Sets.newHashSet(getMethod().getAnnotations());
  }

  @Override
  public A getCacheAnnotation() {
    return (A) methodAnnotation;

  }

  public static CacheMethodType getCacheMethodType(Method method) {

    if (method.isAnnotationPresent(CachePut.class)) {
      return CacheMethodType.CACHE_PUT;
    } else if (method.isAnnotationPresent(CacheResult.class)) {
      return CacheMethodType.CACHE_RESULT;
    } else if (method.isAnnotationPresent(CacheRemove.class)) {
      return CacheMethodType.CACHE_REMOVE_ENTITY;
    }  else if (method.isAnnotationPresent(CacheRemoveAll.class)) {
      return CacheMethodType.CACHE_REMOVE_ALL;
    } else {
      LOGGER.error("Unknown cache annotation {}", method.getAnnotations());
      throw new UnsupportedOperationException("Unsupported annotation");
    }
  }

  @Override
  public String getCacheName() {

    // First check method annotation
    if (null != methodCacheName) {
      return methodCacheName;
    }

    // Check class
    Class clazz = getThisClass(methodInvocation);
    if (clazz.isAnnotationPresent(CacheDefaults.class)) {
      CacheDefaults cacheDefaults = (CacheDefaults) clazz.getAnnotation(CacheDefaults.class);
      if (null != cacheDefaults.cacheName()) {
        return cacheDefaults.cacheName();
      }
    }

    // Use the calls name as default cache name
    return clazz.getName();
  }


  public static Class getThisClass(MethodInvocation methodInvocation) {
    Class clazz = methodInvocation.getThis().getClass();
    if (clazz.getName().contains("$$EnhancerByGuice$$")) {
      // Workaround for Guice adding some kind of enhancer object before the actual DAO
      // Check the superclass
      clazz = clazz.getSuperclass();
    }
    return clazz;
  }

  private static Triplet<Collection<CacheInvocationParameter>, Collection<CacheInvocationParameter>, CacheInvocationParameter> getParameterDetails(final MethodInvocation methodInvocation) {

    final Method method = methodInvocation.getMethod();
    final boolean isCacheValueAllowed = method.isAnnotationPresent(CachePut.class);

    // Get parameter type and annotation details
    final Class<?>[] parameterTypes = method.getParameterTypes();
    final Annotation[][] parameterAnnotations = method.getParameterAnnotations();
    final Object[] arguments = methodInvocation.getArguments();

    // All and key parameter lists
    final Collection<CacheInvocationParameter> allParameters = Lists.newArrayList();
    final Collection<CacheInvocationParameter> keyParameters = Lists.newArrayList();
    CacheInvocationParameter cacheValueParameter = null;

    // Step through each parameter
    for (int pIdx = 0; pIdx < parameterTypes.length; pIdx++) {
      final int position = pIdx;
      final Class<?> rawType = parameterTypes[pIdx];

      // Create Set of annotations on Method and check for @CacheKey
      boolean isKey = false;
      boolean isValue = false;
      final Set<Annotation> annotations = Sets.newHashSet();
      for (final Annotation parameterAnnotation : parameterAnnotations[pIdx]) {
        annotations.add(parameterAnnotation);
        if (CacheKey.class.isAssignableFrom(parameterAnnotation.annotationType())) {
          isKey = true;
        } else if (CacheValue.class.isAssignableFrom(parameterAnnotation.annotationType())) {
          if (!isCacheValueAllowed) {
            throw new AnnotationFormatError("CacheValue parameter annotation is not allowed on " + method);
          } else if (cacheValueParameter != null || isValue) {
            throw new AnnotationFormatError("Multiple CacheValue parameter annotations are not allowed: " + method);
          } else {
            isValue = true;
          }
        }
      }

      // TODO @CacheValue must be present when @CachePut?

      // Create parameter details object
      final CacheInvocationParameter invocationParameter = new CacheInvocationParameter() {
        @Override
        public Class<?> getRawType() {
          return rawType;
        }
        @Override
        public Object getValue() {
          return arguments[position];
        }
        @Override
        public Set<Annotation> getAnnotations() {
          return annotations;
        }
        @Override
        public int getParameterPosition() {
          return position;
        }
      };

      // Add parameter details to List and to key list if it is marked as a cache key parameter
      allParameters.add(invocationParameter);
      if (isKey) {
        keyParameters.add(invocationParameter);
      }
      if (isValue) {
        cacheValueParameter = invocationParameter;
      }
    }

    // If no parameters were marked as key parameters then all parameters will be used as keys
    if (keyParameters.isEmpty()) {
      keyParameters.addAll(allParameters);
    }

    // Remove the value parameter from the key parameter list
    if (cacheValueParameter != null) {
      keyParameters.remove(cacheValueParameter);
    }

    return Triplet.of(allParameters, keyParameters, cacheValueParameter);
  }

}
