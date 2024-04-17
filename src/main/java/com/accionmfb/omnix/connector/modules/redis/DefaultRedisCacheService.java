package com.accionmfb.omnix.connector.modules.redis;

import com.accionmfb.omnix.connector.util.RedisDataRetrievalFallbackOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Objects;

@Slf4j
@Configuration

@RequiredArgsConstructor
public class DefaultRedisCacheService implements RedisCacheService{

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void save(String key, Object value){
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public Object get(String key){
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public <T> T getCasted(String key, Class<T> tClass){
        return tClass.cast(get(key));
    }

    @Override
    public Object getWithFallback(String key, RedisDataRetrievalFallbackOperation operation){
        Object redisRetrieval = get(key);
        if(Objects.isNull(redisRetrieval)){
            return operation.executeFallbackRetrieval(key);
        }
        return redisRetrieval;
    }

    @Override
    public <T> T getCastedWithFallback(String key, Class<T> tClass, RedisDataRetrievalFallbackOperation operation){
        Object retrievalWithFallback = getWithFallback(key, operation);
        return tClass.cast(retrievalWithFallback);
    }

    @Override
    public boolean delete(String key){
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }
}
