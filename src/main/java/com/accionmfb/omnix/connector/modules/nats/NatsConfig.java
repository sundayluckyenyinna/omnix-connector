package com.accionmfb.omnix.connector.modules.nats;

import io.nats.client.Connection;
import io.nats.client.Nats;
import io.nats.client.Options;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(name = "nats.connection.properties.enable", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(value = { OmnixConnectionProperties.class })
public class NatsConfig {

    private final OmnixConnectionProperties properties;
    private final static String DEFAULT_NATS_SERVER_URL = "nats://localhost:4222";

    @Bean
    public Connection omnixNatsConnection() throws IOException, InterruptedException, GeneralSecurityException {
        String configuredServerUrl = properties.getServerUrl();
        String resolvedServerUrl = Objects.isNull(configuredServerUrl) ? DEFAULT_NATS_SERVER_URL : configuredServerUrl;
        logConfiguredServerUrl(resolvedServerUrl);
        Options options = Options.builder()
                .server(properties.getServerUrl())
                .maxReconnects(properties.getMaxReconnect())
                .build();
        try {
            Connection connection = Nats.connect(options);
            log.info("Nats Connection status: {}", connection.getStatus());
            if (connection.getStatus() == Connection.Status.CONNECTED) {
                log.info("\u001B[32m NATS CONNECTION STARTED AND RUNNING...\u001B[0m");
            }else{
                log.error("\u001B[31m{}\u001B[0m", "NATS CONNECTION FAILED TO ESTABLISH");
            }
            return connection;
        }catch (Exception exception){
            log.error("\u001B[31m{}\u001B[0m", "NATS CONNECTION FAILED TO ESTABLISH");
            log.error("Exception occurred while connecting to Nats server. Exception message is: {}", exception.getMessage());
            return null;
        }
    }

    private void logConfiguredServerUrl(String resolvedUrl){
        String logInfo = String.format("Resolved Nats Server URL: %s |", resolvedUrl);
        log.info("-".repeat(logInfo.length()));
        log.info(logInfo);
        log.info("-".repeat(logInfo.length()));
    }
}
