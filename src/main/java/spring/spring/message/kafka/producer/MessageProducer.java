package spring.spring.message.kafka.producer;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

@Slf4j
@Component
public class MessageProducer {

    @Autowired
    KafkaTemplate kafkaTemplate;

    public void messageSend(String topic, Integer partition, String key, Object body) throws ExecutionException, InterruptedException {
        ProducerRecord producerRecord = new ProducerRecord(topic, partition, key, JSON.toJSONString(body));
        ListenableFuture send = kafkaTemplate.send(producerRecord);
        Object o = send.get();
        log.info("messageSend:{}", o);
        kafkaTemplate.flush();
    }
}
