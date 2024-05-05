package com.accionmfb.omnix.connector.modules.redis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "redis.connection.properties")
public class RedisConfigurationProperties {
    private String host = "localhost";
    private String port = "9092";
    private String connectionTimeout = "60";
}
