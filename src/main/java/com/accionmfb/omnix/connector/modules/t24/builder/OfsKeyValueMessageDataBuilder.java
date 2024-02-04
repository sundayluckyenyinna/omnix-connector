package com.accionmfb.omnix.connector.modules.t24.builder;

public interface OfsKeyValueMessageDataBuilder {
    String SINGLE_KEY_VALUE_JOINER = "::=";

    boolean supports(Object incomingValueInstance);
    String build(String key, Object incomingValue);
}
