package spring.spring.source.rocketmq;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface Source {
    String OUTPUT = "output";

    @Output(value = OUTPUT)
    MessageChannel output();
}
