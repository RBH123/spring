package spring.spring.message.rocketmq.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.stereotype.Component;
import spring.spring.source.Sink;

@Slf4j
@Component
@EnableBinding(value = {Sink.class})
public class MessageConsumer {

    @Autowired
    SubscribableChannel input;

    @StreamListener(value = Sink.INTPUT, condition = "headers['tag'] == 'test-tags'")
    public void consumerMessage(String message) {
        System.out.println(message);
    }
}
