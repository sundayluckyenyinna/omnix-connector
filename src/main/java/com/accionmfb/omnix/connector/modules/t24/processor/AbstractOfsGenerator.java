package com.accionmfb.omnix.connector.modules.t24.processor;

import org.springframework.context.annotation.Configuration;

@Configuration
public abstract class AbstractOfsGenerator implements OfsGenerator{

    protected String username;
    protected String password;
    protected String enquiry;
    protected String messageData;
}
