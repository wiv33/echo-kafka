package org.example;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.Scanner;

import static java.lang.System.*;

public class HelloProducer {

    public static final String TOPIC_NAME = "test_log";

    public static void main(String[] args) {
        Properties configs = new Properties();
        String bootstrapServers = getProperty(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG);
        out.println("bootstrapServers = " + bootstrapServers);
        configs.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        KafkaProducer<String, String> producer = new KafkaProducer<>(configs);

        while (true) {
            Scanner sc = new Scanner(in);
            out.print("Input > ");
            String message = sc.nextLine();

            ProducerRecord<String, String> producerRecord = new ProducerRecord<>(TOPIC_NAME, message);
            try {
                producer.send(producerRecord, (metadata, exception) -> {
                    if (exception != null) {
                        // some exception
                        out.println("metadata = " + metadata);
                        out.println(exception.getMessage());
                    }
                });

            } catch (Exception e) {
                // exception
            } finally {
                producer.flush();
            }

            if ("0".equals(message)) {
                producer.close();
                break;
            }
        }
    }
}
