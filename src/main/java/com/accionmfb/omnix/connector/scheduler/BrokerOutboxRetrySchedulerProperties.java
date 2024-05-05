package com.accionmfb.omnix.connector.scheduler;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "broker.outbox.retry")
public class BrokerOutboxRetrySchedulerProperties {
    private boolean enable = false;
}
