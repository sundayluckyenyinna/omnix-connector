package com.accionmfb.omnix.connector.modules.nats;

import com.accionmfb.omnix.connector.modules.OmnixStreamPayload;
import com.accionmfb.omnix.connector.util.FallbackBrokerOperation;

public interface NatsService {
    void publishMessage(Object subject, OmnixStreamPayload<?> omnixStreamPayload);

    void publishMessageWithFallback(Object subject, OmnixStreamPayload<?> omnixStreamPayload, FallbackBrokerOperation fallbackBrokerOperation);
}
