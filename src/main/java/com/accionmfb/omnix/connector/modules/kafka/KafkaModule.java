package com.accionmfb.omnix.connector.modules.kafka;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration

@Import({
        DefaultKafkaBrokerService.class,
        OmnixKafkaConfig.class,
})
public class KafkaModule {
}
