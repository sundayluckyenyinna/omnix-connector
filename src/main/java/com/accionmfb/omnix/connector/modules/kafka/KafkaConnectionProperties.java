package com.accionmfb.omnix.connector.modules.kafka;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "kafka.connection.properties")
public class KafkaConnectionProperties {
    private String bootstrapServers = "localhost:9092";
    private String groupId = "consumer-group-id";
    private boolean enable = false;
}
