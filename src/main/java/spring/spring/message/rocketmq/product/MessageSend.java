package spring.spring.message.rocketmq.product;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import spring.spring.source.Source;

import java.util.Map;

@Slf4j
@Component
@EnableBinding(value = {Source.class})
public class MessageSend {

    @Autowired
    MessageChannel output;

    public boolean sendMessage(String tag, String key, Object body) {
        Map<String, Object> headers = Maps.newHashMap();
        headers.put(MessageConst.PROPERTY_TAGS, tag);
        headers.put(MessageConst.PROPERTY_KEYS, key);
        boolean send = output.send(MessageBuilder.createMessage(JSON.toJSONString(body), new MessageHeaders(headers)));
        return send;
    }
}
