package com.accionmfb.omnix.connector.modules.ws.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface WebSocketIgnore {
}
