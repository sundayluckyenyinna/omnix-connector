package com.accionmfb.omnix.connector.modules.redis;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration

@Import({
        DefaultRedisCacheService.class,
        OmnixRedisConfig.class
})
public class RedisModule {
}
