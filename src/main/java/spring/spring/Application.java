package spring.spring;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import spring.spring.message.kafka.producer.MessageProduct;

import javax.annotation.PostConstruct;

@MapperScans(value = {@MapperScan(basePackages = "spring.spring.dao")})
@RefreshScope
@SpringBootApplication
public class Application {

    @Autowired
    MessageProduct messageSend;

//    @Value("${name}")
//    private String name;

    @PostConstruct
    public void init() {
        messageSend.sendMessage("tagA", "test-key", "今天天气好晴朗");
//        System.out.println(name);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
