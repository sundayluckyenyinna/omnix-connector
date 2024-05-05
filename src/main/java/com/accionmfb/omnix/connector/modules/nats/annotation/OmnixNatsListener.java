package com.accionmfb.omnix.connector.modules.nats.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OmnixNatsListener {
    String subject();
}
