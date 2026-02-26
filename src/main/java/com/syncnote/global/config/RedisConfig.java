package com.syncnote.global.config;

import com.syncnote.domain.auth.dto.RefreshTokenCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.net.URI;

@Configuration
public class RedisConfig {
    @Value("${spring.data.redis.host:}")
    private String host;

    @Value("${spring.data.redis.port:6379}")
    private int port;

    @Value("${spring.data.redis.password:}")
    private String password;

    @Value("${spring.data.redis.url:}")
    private String redisUrl;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        if (redisUrl != null && !redisUrl.isBlank()) {
            URI uri = URI.create(redisUrl);

            String uriHost = uri.getHost();
            int uriPort = (uri.getPort() == -1) ? 6379 : uri.getPort();

            RedisStandaloneConfiguration config =
                    new RedisStandaloneConfiguration(uriHost, uriPort);

            String userInfo = uri.getUserInfo();

            if (userInfo != null && !userInfo.isBlank()) {
                String password = userInfo.contains(":")
                        ? userInfo.substring(userInfo.indexOf(':') + 1)
                        : userInfo;

                if (!password.isBlank()) {
                    config.setPassword(RedisPassword.of(password));
                }
            }

            boolean ssl = "rediss".equalsIgnoreCase(uri.getScheme());

            LettuceClientConfiguration clientConfig = ssl
                    ? LettuceClientConfiguration.builder().useSsl().build()
                    : LettuceClientConfiguration.builder().build();

            return new LettuceConnectionFactory(config, clientConfig);
        }

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
