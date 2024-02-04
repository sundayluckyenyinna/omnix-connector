package com.accionmfb.omnix.connector.modules.t24.builder;

public interface OfsKeyValueMessageDataBuilder {
    boolean supports(Object incomingValueInstance);
    String build(String key, Object incomingValue);
}
