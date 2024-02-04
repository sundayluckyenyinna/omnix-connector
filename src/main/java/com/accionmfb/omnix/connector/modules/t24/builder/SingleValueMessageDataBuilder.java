package com.accionmfb.omnix.connector.modules.t24.builder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SingleValueMessageDataBuilder implements OfsKeyValueMessageDataBuilder{

    @Override
    public boolean supports(Object incomingValueInstance) {
        return incomingValueInstance instanceof String;
    }

    @Override
    public String build(String key, Object incomingValue) {
        return key.concat(SINGLE_KEY_VALUE_JOINER).concat(String.valueOf(incomingValue));
    }
}
