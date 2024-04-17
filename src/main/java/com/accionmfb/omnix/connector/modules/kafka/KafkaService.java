package com.accionmfb.omnix.connector.modules.kafka;

import com.accionmfb.omnix.connector.util.FallbackKafkaOperation;

public interface KafkaService {
    void publishMessage(Object topic, OmnixKafkaPayload omnixKafkaPayload);

    void publishMessage(Object topic, String key, OmnixKafkaPayload omnixKafkaPayload);

    void publishMessageWithFallback(Object topic, OmnixKafkaPayload omnixKafkaPayload, FallbackKafkaOperation operation);

    void publishMessageWithFallback(Object topic, String key, OmnixKafkaPayload omnixKafkaPayload, FallbackKafkaOperation operation);
}
