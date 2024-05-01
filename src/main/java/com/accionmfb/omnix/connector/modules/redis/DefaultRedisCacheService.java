package com.accionmfb.omnix.connector.modules.redis;

import com.accionmfb.omnix.connector.util.ParameterizedRedisDataRetrievalFallbackOperation;
import com.accionmfb.omnix.connector.util.RedisDataRetrievalFallbackOperation;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Objects;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DefaultRedisCacheService implements RedisCacheService{

    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void save(String key, Object value){
        redisTemplate.opsForValue().set(key, resolveRedisValue(value));
        logSaveOperation(key, value);
    }

    @Override
    public Object get(String key){
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public <T> T getCasted(String key, Class<T> tClass){
        Object redisValue = get(key);
        return Objects.isNull(redisValue) ? null : tClass.cast(redisValue);
    }

    @Override
    public <T> T getDeserialized(String key, Class<T> tClass){
        Object redisValue = get(key);
        return Objects.isNull(redisValue) ? null : resolveRedisValueDeserialized(redisValue, tClass);
    }

    @Override
    public <T> T getDeserializedWithFallback(String key, Class<T> tClass, ParameterizedRedisDataRetrievalFallbackOperation<T> fallbackOperation){
        T redisRetrieval = getDeserialized(key, tClass);
        if(Objects.isNull(redisRetrieval)){
            logRedisFallOperationReport(key);
            return (T) fallbackOperation.fallback();
        }
        return redisRetrieval;
    }

    @Override
    public Object getWithFallback(String key, RedisDataRetrievalFallbackOperation operation){
        Object redisRetrieval = get(key);
        if(Objects.isNull(redisRetrieval)){
            logRedisFallOperationReport(key);
            return operation.executeFallbackRetrieval();
        }
        return redisRetrieval;
    }

    @Override
    public <T> T getCastedWithFallback(String key, Class<T> tClass, RedisDataRetrievalFallbackOperation operation){
        Object retrievalWithFallback = getWithFallback(key, operation);
        return Objects.isNull(retrievalWithFallback) ? null : tClass.cast(retrievalWithFallback);
    }

    @Override
    public boolean delete(String key){
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    @SneakyThrows
    private String resolveRedisValue(Object value){
        return value instanceof String ? (String) value : objectMapper.writeValueAsString(value);
    }

    @SneakyThrows
    private <T> T resolveRedisValueDeserialized(Object returnedValue, Class<T> tClass){
        String valueJson = returnedValue instanceof String ? (String) returnedValue : objectMapper.writeValueAsString(returnedValue);
        return objectMapper.readValue(valueJson, tClass);
    }

    private void logRedisFallOperationReport(String key){
        log.info("No data value found in redis cache with key: {}", key);
        log.info("System will execute and return the value of the fallback operation");
    }

    private void logSaveOperation(String key, Object value){
        log.info("Item saved to redis cache successfully with key: {} and value: {}", key, value);
    }
}
