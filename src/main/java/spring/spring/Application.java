package spring.spring;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import spring.spring.message.kafka.producer.MessageProducer;
import spring.spring.message.rocketmq.producer.MessageProduct;

import javax.annotation.PostConstruct;

@EnableDiscoveryClient
@MapperScans(value = {@MapperScan(basePackages = "spring.spring.dao")})
@RefreshScope
@SpringBootApplication
public class Application {

    @Autowired
    MessageProduct messageSend;
    @Autowired
    MessageProducer messageProducer;

    @PostConstruct
    public void init() throws Exception {
        messageSend.sendMessage("tagA", "test-key", "今天天气好晴朗");
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
