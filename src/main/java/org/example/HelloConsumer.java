package org.example;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import static java.lang.System.*;
import static org.example.HelloProducer.TOPIC_NAME;

public class HelloConsumer {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, TOPIC_NAME);

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        try (consumer) {
            consumer.subscribe(Collections.singletonList(TOPIC_NAME));
            String message = null;
            do {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(3));

                for (ConsumerRecord<String, String> consumerRecord : records) {
                    message = consumerRecord.value();
                    out.println(message);
                }
            } while ("0".equalsIgnoreCase(message));
        } catch (Exception e) {
            // exception
        }
    }
}
