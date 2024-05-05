package com.accionmfb.omnix.connector.modules.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Protocol;

import java.time.Duration;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(value = { RedisConfigurationProperties.class })
public class OmnixRedisConfig {

    private final RedisConfigurationProperties redisConfigurationProperties;

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {

        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(redisConfigurationProperties.getHost());
        redisStandaloneConfiguration.setPort(Integer.parseInt(redisConfigurationProperties.getPort()));

        JedisClientConfiguration.JedisClientConfigurationBuilder jedisClientConfiguration = JedisClientConfiguration.builder();
        jedisClientConfiguration.connectTimeout(Duration.ofSeconds(Integer.parseInt(redisConfigurationProperties.getConnectionTimeout())));

        JedisConnectionFactory connectionFactory = new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration.build());
        try (Jedis jedis = new Jedis(redisStandaloneConfiguration.getHostName(), 6379)) {
            new String((byte[]) jedis.sendCommand(Protocol.Command.INFO));
                log.info("\u001B[32m REDIS CONNECTION STARTED AND RUNNING...\u001B[0m");
        }catch (Exception exception){
            log.error("\u001B[31m{}\u001B[0m", "REDIS CONNECTION FAILED TO ESTABLISH");
            log.debug("Redis connection error is: {}", exception.getMessage());
        }
        return connectionFactory;
    }

    @Bean
    public RedisTemplate<String, Object> omnixRedisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        return template;
    }

}
