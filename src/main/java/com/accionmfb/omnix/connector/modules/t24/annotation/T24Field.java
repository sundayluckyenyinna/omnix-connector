package com.accionmfb.omnix.connector.modules.t24.annotation;

import com.accionmfb.omnix.connector.commons.StringValues;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface T24Field {
    String value() default StringValues.EMPTY_STRING;
    boolean ignoreOnNullValue() default true;
    String defaultForNull() default StringValues.EMPTY_STRING;
}
