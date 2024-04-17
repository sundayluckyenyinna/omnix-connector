package com.accionmfb.omnix.connector.util;

@FunctionalInterface
public interface FallbackKafkaOperation {

    void runFallbackOperation();
}
