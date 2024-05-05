package com.accionmfb.omnix.connector.modules.redis;

import com.accionmfb.omnix.connector.util.ParameterizedRedisDataRetrievalFallbackOperation;
import com.accionmfb.omnix.connector.util.RedisDataRetrievalFallbackOperation;

import java.util.concurrent.TimeUnit;

public interface RedisCacheService {
    void save(String key, Object value);

    void saveWithExpiry(String key, Object value, long expiryInSec);

    void saveWithExpiry(String key, Object value, long expiry, TimeUnit timeUnit);

    Object get(String key);

    <T> T getCasted(String key, Class<T> tClass);

    <T> T getDeserialized(String key, Class<T> tClass);

    <T> T getDeserializedWithFallback(String key, Class<T> tClass, ParameterizedRedisDataRetrievalFallbackOperation<T> fallbackOperation);

    Object getWithFallback(String key, RedisDataRetrievalFallbackOperation operation);

    <T> T getCastedWithFallback(String key, Class<T> tClass, RedisDataRetrievalFallbackOperation operation);

    boolean delete(String key);
}
