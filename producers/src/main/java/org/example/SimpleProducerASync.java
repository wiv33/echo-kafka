package org.example;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.Properties;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SimpleProducerASync {
  public static final String CONFLUENT_BROKER = System.getenv("CONFLUENT_BROKER");
  public static final String TOPIC = "http-logs";
  private static final Logger logger = LoggerFactory.getLogger(SimpleProducerASync.class.getName());

  public static void main(String[] args) {
    // kafka-topics --bootstrap-server kafka-0-internal.confluent.svc.cluster.local:9092 --create --topic simple-topic --partitions 1 --replication-factor 1
    /* create send kafka message example */
    var props = new Properties();
    // bootstrap.servers, key.serializer.class, value.serializer.class
    props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, CONFLUENT_BROKER);
    props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
    props.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

    // send to kafka message key null and value "hello world"
    var producer = new org.apache.kafka.clients.producer.KafkaProducer<String, String>(props);

    var msg = "hello world 2";
    var helloWorld = new ProducerRecord<String, String>(TOPIC, msg);

    try {
    producer.send(helloWorld, (metadata, exception) -> {
      if (exception != null) {
        exception.printStackTrace();
      } else {
        logger.info("\n##### record metadata received \n" +
            "Topic: " + metadata.topic() + "\n" +
            "Partition: " + metadata.partition() + "\n" +
            "Offset: " + metadata.offset() + "\n" +
            "Timestamp: " + metadata.timestamp());
      }

    }).get();
    Thread.sleep(3000L);
    } catch (Exception e) {
      e.printStackTrace();
    }


  }
}