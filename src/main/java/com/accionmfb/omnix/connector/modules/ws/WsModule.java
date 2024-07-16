package com.accionmfb.omnix.connector.modules.ws;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        SimpleServerSocketIOTemplate.class,
        SocketIOConfig.class
})
public class WsModule {
}
