package com.accionmfb.omnix.connector.modules.t24.builder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MultiValueMessageDataBuilder implements OfsKeyValueMessageDataBuilder{
    @Override
    public boolean supports(Object incomingValueInstance) {
        return incomingValueInstance instanceof List<?>;
    }

    @Override
    public String build(String key, Object incomingValue) {
        return null;
    }
}
