package com.accionmfb.omnix.connector.util;

@FunctionalInterface
public interface FallbackBrokerOperation {

    void runFallbackOperation();
}
