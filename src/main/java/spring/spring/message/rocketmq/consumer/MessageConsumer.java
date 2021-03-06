package spring.spring.message.rocketmq.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import spring.spring.source.rocketmq.Sink;

@Slf4j
@EnableBinding(Sink.class)
public class MessageConsumer {

    @StreamListener(value = Sink.INPUT)
    public void consumerMessage(String body) {
        log.info("消费消息:{}", body);
    }
}
