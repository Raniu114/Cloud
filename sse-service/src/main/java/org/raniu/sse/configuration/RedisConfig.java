package org.raniu.sse.configuration;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.raniu.sse.redisListener.RedisHashListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @projectName: IOTCloud
 * @package: org.raniu.sse.configuration
 * @className: RedisConfiguration
 * @author: Raniu
 * @description: TODO
 * @date: 2024/7/1 15:24
 * @version: 1.0
 */

@Configuration
public class RedisConfig {

    @Autowired
    private RedisHashListener redisHashListener;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Bean
    RedisMessageListenerContainer container() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(redisHashListener, RedisHashListener.TOPIC);
        return container;
    }

}
