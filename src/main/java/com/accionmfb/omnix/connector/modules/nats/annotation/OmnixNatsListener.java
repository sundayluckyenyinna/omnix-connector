package com.accionmfb.omnix.connector.modules.nats.annotation;

import org.springframework.context.annotation.Bean;

import java.lang.annotation.*;

@Documented
@Bean(autowireCandidate = false)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OmnixNatsListener {
    String subject();
}
