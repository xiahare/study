package xl.examples.redis;

import java.util.LinkedHashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableCaching
public class RedisConfig {

    private final RedisSerializer<String> redisSerializer = new StringRedisSerializer();
    private final Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);

    @Bean
    public RedisTemplate<String, Object> redisObjectTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        ObjectMapper om = new ObjectMapper();

        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

        jackson2JsonRedisSerializer.setObjectMapper(om);

        template.setConnectionFactory(factory);
        // key serialize
        template.setKeySerializer(redisSerializer);
        // value serialize
        template.setValueSerializer(jackson2JsonRedisSerializer);
        // key hashmap serialize
        template.setHashKeySerializer(redisSerializer);
        // value hashmap serialize
        template.setHashValueSerializer(jackson2JsonRedisSerializer);

        // set default serializer to jackson
        template.setEnableDefaultSerializer(true);
        template.setDefaultSerializer(jackson2JsonRedisSerializer);

        return template;
    }
}
