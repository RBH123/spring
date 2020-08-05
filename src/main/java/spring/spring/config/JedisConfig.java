package spring.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;

/**
 * All rights Reserved, Designed By www.shengyefinance.com
 *
 * @Desc：
 * @Package：spring.spring.config
 * @Title: JedisConfig
 * @Author: rocky.ruan
 * @Date: 2020/8/4 16:49
 * @Copyright: 2018 www.shengyefinance.com Inc. All rights reserved.
 */
@Configuration
public class JedisConfig {

    private Jedis jedis;

    @Bean
    public Jedis jedis() {
        jedis = new Jedis("192.168.17.14", 6379);
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(8);
        jedisPoolConfig.setMaxWaitMillis(2);
        jedisPoolConfig.setMaxTotal(8);
        jedisPoolConfig.setMinIdle(2);
        jedisPoolConfig.setNumTestsPerEvictionRun(3);
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofMinutes(5).toMillis());
        JedisPool jedisPool = new JedisPool(jedisPoolConfig);
        jedis.setDataSource(jedisPool);
        return jedis;
    }
}
