package com.syncnote.global.config;

import com.syncnote.domain.auth.dto.RefreshTokenCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.password:}")
    private String password;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config =
                new RedisStandaloneConfiguration(host, port);

        if (password != null && !password.isBlank()) {
            config.setPassword(RedisPassword.of(password));
        }

        return new LettuceConnectionFactory(config);
    }

    @Bean(name = "refreshTokenRedisTemplate")
    public RedisTemplate<String, RefreshTokenCache> refreshTokenRedisTemplate(
            RedisConnectionFactory connectionFactory
    ) {
        RedisTemplate<String, RefreshTokenCache> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        StringRedisSerializer keySerializer = new StringRedisSerializer();
        JacksonJsonRedisSerializer<RefreshTokenCache> valueSerializer =
                new JacksonJsonRedisSerializer<>(RefreshTokenCache.class);

        template.setKeySerializer(keySerializer);
        template.setValueSerializer(valueSerializer);

        template.setHashKeySerializer(keySerializer);
        template.setHashValueSerializer(valueSerializer);

        template.afterPropertiesSet();
        return template;
    }
}
