package com.accionmfb.omnix.connector.modules.kafka;

import com.accionmfb.omnix.connector.modules.OmnixStreamPayload;
import com.accionmfb.omnix.connector.util.FallbackBrokerOperation;

public interface KafkaService {
    void publishMessage(Object topic, OmnixStreamPayload<?> omnixStreamPayload);

    void publishMessage(Object topic, String key, OmnixStreamPayload<?> omnixStreamPayload);

    void publishMessageWithFallback(Object topic, OmnixStreamPayload<?> omnixStreamPayload, FallbackBrokerOperation operation);

    void publishMessageWithFallback(Object topic, String key, OmnixStreamPayload<?> omnixStreamPayload, FallbackBrokerOperation operation);
}
