package com.mf.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class EntityUtil {
    private static final Logger logger = LoggerFactory.getLogger(EntityUtil.class);

    public static <T> T getProperty(Object obj, String name) {
        T result = null;

        try {
            StringBuffer methodName = new StringBuffer();
            String prefix = name.substring(0, 1).toUpperCase();

            methodName.append("get").append(prefix).append(name.substring(1));
            Method method = obj.getClass().getDeclaredMethod(methodName.toString(), new Class[]{});

            result = (T) method.invoke(obj, null);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            return result;
        }
    }
}
