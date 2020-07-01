/**
 * = = = = = = = = = = = = = = = = =
 * Copyright (C) 2019 - Sagemcom Energy & Telecom SAS All rights reserved
 * Date 2019-05-26
 * This Copyright notice should not be removed
 * = = = = = = = = = = = = = = = = =
 */
package com.example.demo.util;


import com.example.demo.error.DemoAppException;
import com.example.demo.error.ErrorType;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;

@Log4j2
public class LangUtils {
    public interface ThrowingMethod {
        void call() throws Exception;
    }

    public interface ThrowingSupplier<T> {
        T call() throws Exception;
    }

    public static void sneakyThrows(ThrowingMethod throwingMethod) {
        try {
            throwingMethod.call();
        } catch (DemoAppException e) {
            throw e;
        } catch (Exception e) {
            throw new DemoAppException(ErrorType.INTERNAL,e);
        }
    }

    public static <T> T sneakyThrows(ThrowingSupplier<T> throwingSupplier) {
        try {
            return throwingSupplier.call();
        } catch (DemoAppException e) {
            throw e;
        } catch (Exception e) {
            throw new DemoAppException(ErrorType.INTERNAL,e);
        }
    }

    public static void swallowThrows(ThrowingMethod throwingMethod) {
        try {
            throwingMethod.call();
        } catch (Exception e) {
            log.error("", e);
        }
    }

    public static String loadText(Class clazz, String fileName) {
        return sneakyThrows(() -> {
            try (InputStream is = clazz.getResourceAsStream(fileName)) {
                return IOUtils.toString(is, "UTF8");
            }
        });
    }

}

