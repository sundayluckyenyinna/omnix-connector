package com.accionmfb.omnix.connector.modules.t24.processor;

import com.accionmfb.omnix.connector.modules.t24.constants.OfsRequestType;

public interface OfsGenerator {

    String generate();
    boolean supports(OfsRequestType ofsRequestType);
}
