package com.accionmfb.omnix.connector.modules.ws;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "websocket.server")
public class ServerSocketIOConfigurationProperties {
    private boolean expose = false;
    private String hostName = "localhost";
    private Integer port = 2007;
    private boolean showConnectionLogs = true;
}
