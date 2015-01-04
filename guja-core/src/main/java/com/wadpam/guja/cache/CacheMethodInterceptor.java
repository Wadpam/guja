package com.wadpam.guja.cache;

import net.sf.mardao.dao.AbstractDao;
import net.sf.mardao.dao.Cached;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * Created by sosandstrom on 2015-01-02.
 */
public class CacheMethodInterceptor implements MethodInterceptor {

    static final Logger LOGGER = LoggerFactory.getLogger(CacheMethodInterceptor.class);

    static Method readMethod, pageMethod;

    static {
        try {
            pageMethod = AbstractDao.class.getMethod("queryPage", int.class, String.class);
//            readMethod = AbstractDao.class.getMethod("get");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        final Object[] args = invocation.getArguments();
        final Method method = invocation.getMethod();
        LOGGER.info("invoking on {}, isAnnotated {}", method, method.isAnnotationPresent(Cached.class));
        if (method.equals(readMethod)) {
            LOGGER.info("   read");
        }
        else if (method.equals(pageMethod)) {
            Integer pageSize = (Integer) args[0];
            String cursorString = (String) args[1];

            LOGGER.info("   page {} {}", pageSize, cursorString);
        }
//        LOGGER.info("present on {} / {}", daoClass.isAnnotationPresent(Cached.class), method.isAnnotationPresent(Cached.class));
        return invocation.proceed();
    }
}
