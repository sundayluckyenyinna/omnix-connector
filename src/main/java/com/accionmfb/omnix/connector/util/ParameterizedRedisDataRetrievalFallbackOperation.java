package com.accionmfb.omnix.connector.util;

@FunctionalInterface
public interface ParameterizedRedisDataRetrievalFallbackOperation<T> {

    T fallback();
}
