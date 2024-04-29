package com.accionmfb.omnix.connector.modules.nats;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "nats.connection.properties")
public class OmnixConnectionProperties  {
    private String serverUrl = "nats://localhost:4222";
    private int maxReconnect = 60;
    private boolean enable = false;
}
