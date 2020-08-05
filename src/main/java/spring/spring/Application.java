package spring.spring;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.SneakyThrows;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.redis.core.RedisTemplate;
import spring.spring.dao.entity.Users;
import spring.spring.elasticsearch.ElasticsearchService;
import spring.spring.message.kafka.producer.MessageProducer;
import spring.spring.message.rocketmq.producer.MessageProduct;
import spring.spring.util.DistributedUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@ComponentScan(basePackages = "spring.spring.*")
@EnableAspectJAutoProxy(proxyTargetClass = true)
@MapperScan(basePackages = "spring.spring.dao")
@EnableDiscoveryClient
@RefreshScope
@SpringBootApplication
public class Application {

    @Autowired
    MessageProduct messageSend;
    @Autowired
    MessageProducer messageProducer;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    ElasticsearchService elasticsearchService;
    @Autowired
    DistributedUtils distributedUtils;

    @PostConstruct
    @SneakyThrows
    public void init() {
        XContentBuilder xContentBuilder = XContentFactory.jsonBuilder()
                .startObject()
                .startObject("properties")
                .startObject("username")
                .field("type", "keyword")
                .endObject()
                .startObject("mobile")
                .field("type", "keyword")
                .endObject()
                .startObject("email")
                .field("type", "keyword")
                .endObject()
                .endObject()
                .endObject();
//        elasticsearchService.createIndex(ElasticsearchIndexEnum.DATA_INDEX.getIndex() + "1", xContentBuilder);

        Users users = Users.builder().username("张六").mobile("19866732217").email("1692100705@qq.com").build();
        Users users1 = Users.builder().username("张四").mobile("19866732217").email("1692100705@qq.com").build();
        Users users2 = Users.builder().username("王五").mobile("19866732217").email("1692100705@qq.com").build();
        List<Users> arrayList = Lists.newArrayList(users, users1, users2);
//        elasticsearchService.addBatchData(ElasticsearchIndexEnum.DATA_INDEX.getIndex(), arrayList);
//        Users users = elasticsearchService.queryDataOne(Users.class, "张三");
//        System.out.println(users);
        Map<String, Object> maps = Maps.newHashMap();
        maps.put("username", "张");
        messageSend.sendMessage("tagA", "test-key", "今天天气好晴朗");
//        ThreadPoolExecutor executor = ThreadPoolUtils.getInstance();
//        for (int i = 0; i < 10; i++) {
//            executor.execute(() -> {
//                try {
//                    boolean lock = distributedUtils.tryLock("lock-key", "lock-value", 100000);
//                    System.out.println(Thread.currentThread().getName() + "获得锁状态:" + lock);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            });
//        }
//        distributedUtils.tryLock("lock-key", "lock-value", 100000);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
