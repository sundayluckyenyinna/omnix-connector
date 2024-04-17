package com.accionmfb.omnix.connector.modules.redis;

import com.accionmfb.omnix.connector.util.RedisDataRetrievalFallbackOperation;

public interface RedisCacheService {
    void save(String key, Object value);

    Object get(String key);

    <T> T getCasted(String key, Class<T> tClass);

    Object getWithFallback(String key, RedisDataRetrievalFallbackOperation operation);

    <T> T getCastedWithFallback(String key, Class<T> tClass, RedisDataRetrievalFallbackOperation operation);

    boolean delete(String key);
}
