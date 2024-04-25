package com.accionmfb.omnix.connector.modules.http;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration

@Import({
        OmnixDefaultWebClient.class,
        WebClientLogger.class,
        OmnixRestTemplateConfig.class
})
public class HttpModule {
}
