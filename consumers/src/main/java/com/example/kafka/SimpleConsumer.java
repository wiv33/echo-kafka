package com.example.kafka;

import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Properties;

public class SimpleConsumer {
  public static final String TOPIC_NAME = "simple-topic";

  // logger
  private static final Logger logger = LoggerFactory.getLogger(SimpleConsumer.class.getName());

  public static void main(String[] args) {
    var props = new Properties();
    props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, System.getenv("CONFLUENT_BROKER"));
    props.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    props.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    props.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "group_01");

    KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
    consumer.subscribe(List.of(TOPIC_NAME));
    while (true) {
      var records = consumer.poll(Duration.of(1, ChronoUnit.SECONDS));
      records.forEach(record -> {
        logger.info("key: {}, value: {}, partition: {}", record.key(), record.value(), record.partition());
      });

    }
  }
}
