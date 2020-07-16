package spring.spring.message.kafka.producer;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageConst;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import spring.spring.source.rocketmq.Source;

import javax.annotation.Resource;

@Slf4j
@Component
@EnableBinding(Source.class)
public class MessageProduct {

    @Resource
    @Qualifier(value = "output")
    MessageChannel kafkaOutput;

    public boolean sendMessage(String tag, String key, Object body) {
        boolean send = kafkaOutput.send(MessageBuilder.withPayload(JSON.toJSONString(body))
                .setHeader(MessageConst.PROPERTY_TAGS, tag)
                .setHeader(MessageConst.PROPERTY_KEYS, key)
                .build());
        return send;
    }
}
