package spring.spring.source.rocketmq;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface Sink {
    String INPUT = "input";

    @Input(value = INPUT)
    SubscribableChannel input();
}
