package spring.spring;

import lombok.SneakyThrows;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import spring.spring.constant.ElasticsearchIndexEnum;
import spring.spring.dao.entity.Users;
import spring.spring.elasticsearch.ElasticsearchService;
import spring.spring.message.kafka.producer.MessageProducer;
import spring.spring.message.rocketmq.producer.MessageProduct;

import javax.annotation.PostConstruct;

@MapperScan(basePackages = "spring.spring.dao")
@EnableDiscoveryClient
@RefreshScope
@SpringBootApplication(scanBasePackages = {"spring.spring"})
public class Application {

    @Autowired
    MessageProduct messageSend;
    @Autowired
    MessageProducer messageProducer;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    ElasticsearchService elasticsearchService;

    @PostConstruct
    @SneakyThrows
    public void init() {
        XContentBuilder xContentBuilder = XContentFactory.jsonBuilder()
                .startObject()
                .startObject("properties")
                .startObject("username")
                .field("type", "text")
                .field("index", "true")
                .endObject()
                .startObject("mobile")
                .field("type", "keyword")
                .endObject()
                .startObject("email")
                .field("type", "keyword")
                .endObject()
                .endObject()
                .endObject();
//        elasticsearchService.createIndex(ElasticsearchIndexEnum.DATA_INDEX.getIndex(), xContentBuilder);
        Users users = Users.builder().username("张三").mobile("19866732217").email("1692100705@qq.com").build();
        elasticsearchService.addData(ElasticsearchIndexEnum.DATA_INDEX.getIndex(), users);
        messageSend.sendMessage("tagA", "test-key", "今天天气好晴朗");
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
