package com.accionmfb.omnix.connector.modules.nats;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Slf4j
@Configuration
@Import({
        NatsConfig.class,
        DefaultNatsService.class,
        NatsConsumerAutoConfiguration.class
})
@RequiredArgsConstructor
public class NatsModule {
}
