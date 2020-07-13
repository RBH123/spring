package spring.spring.source;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface Sink {

    String INTPUT = "input";

    @Input(value = INTPUT)
    SubscribableChannel input();
}
