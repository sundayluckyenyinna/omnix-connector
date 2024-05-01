package com.accionmfb.omnix.connector.util;

@FunctionalInterface
public interface RedisDataRetrievalFallbackOperation {

    Object executeFallbackRetrieval();
}
