package spring.spring.config;

import lombok.SneakyThrows;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * All rights Reserved, Designed By www.shengyefinance.com
 *
 * @Desc：
 * @Package：spring.spring.config
 * @Title: ZookeeperConfig
 * @Author: rocky.ruan
 * @Date: 2020/8/3 16:24
 * @Copyright: 2018 www.shengyefinance.com Inc. All rights reserved.
 */
@Configuration
public class ZookeeperConfig {

    final int TIMEOUT = 60000;

    @Value("${zookeeper.host}")
    private String host;

    private ZooKeeper zooKeeper;

    @Bean
    @SneakyThrows
    public ZooKeeper zkClientConfig() {
        zooKeeper = new ZooKeeper(host, TIMEOUT, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
            }
        });
        return zooKeeper;
    }
}
