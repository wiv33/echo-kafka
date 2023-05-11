package org.example;

import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.LinkedList;
import java.util.Properties;

public class SimpleProducer {
  public static final String CONFLUENT_BROKER = System.getenv("CONFLUENT_BROKER");

  public static final String TOPIC = "simple-topic";

  public static void main(String[] args) {
    // kafka-topics --bootstrap-server kafka-0-internal.confluent.svc.cluster.local:9092 --create --topic simple-topic --partitions 1 --replication-factor 1
    /* create send kafka message example */
    var props = new Properties();
    // bootstrap.servers, key.serializer.class, value.serializer.class
    props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
    props.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

    // send to kafka message key null and value "hello world"
    var producer = new org.apache.kafka.clients.producer.KafkaProducer<String, String>(props);
    producer.send(new org.apache.kafka.clients.producer.ProducerRecord<String, String>(TOPIC, "hello world"));


    producer.flush();
    producer.close();
  }
}