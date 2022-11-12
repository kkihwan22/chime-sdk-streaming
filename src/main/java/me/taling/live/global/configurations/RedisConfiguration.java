package me.taling.live.global.configurations;

import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@ConfigurationProperties(prefix = "redis")
@Configuration
@EnableRedisRepositories
public class RedisConfiguration {
    private final Logger log = LoggerFactory.getLogger(RedisConfiguration.class);

    @Setter
    private String host;

    @Setter
    private int port;

    @Setter
    private String publishHost;

    @Setter
    private int publishPort;


    @Primary
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        log.debug("host:{}, port:{}", host, port);
        return new LettuceConnectionFactory(host, port);
    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        GenericToStringSerializer genericToStringSerializer = new GenericToStringSerializer(Object.class);
        redisTemplate.setKeySerializer(genericToStringSerializer);
        redisTemplate.setValueSerializer(genericToStringSerializer);
        return redisTemplate;
    }

    @Bean
    public RedisConnectionFactory pubRedisConnectionFactory() {
        log.debug("host:{}, port:{}", publishHost, publishPort);
        return new LettuceConnectionFactory(publishHost, publishPort);
    }

    @Bean
    public RedisTemplate<String, String> pubRedisTemplate() {
        RedisTemplate<String, String> pubRedisTemplate = new RedisTemplate<>();
        pubRedisTemplate.setKeySerializer(new StringRedisSerializer());
        pubRedisTemplate.setValueSerializer(new StringRedisSerializer());
        pubRedisTemplate.setConnectionFactory(pubRedisConnectionFactory());
        return pubRedisTemplate;
    }
}