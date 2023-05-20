package org.example;

import java.util.Properties;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ProducerASyncCustomCB {

  public static final String CONFLUENT_BROKER = System.getenv("CONFLUENT_BROKER");
  public static final String TOPIC = "multipart-topic";
  private static final Logger logger = LoggerFactory.getLogger(
    ProducerASyncCustomCB.class.getName());

  public static void main(String[] args) throws InterruptedException {
    // kafka-topics --bootstrap-server kafka-0-internal.confluent.svc.cluster.local:9092 --create --topic simple-topic --partitions 1 --replication-factor 1
    /* create send kafka message example */
    var props = new Properties();
    // bootstrap.servers, key.serializer.class, value.serializer.class
    props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, CONFLUENT_BROKER);
    props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
      IntegerSerializer.class.getName());
    props.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
      StringSerializer.class.getName());

    // send to kafka message key null and value "hello world"
    var producer = new org.apache.kafka.clients.producer.KafkaProducer<Integer, String>(props);

    for (int i = 0; i < 20; i++) {
      var msg = "hello world 2";
      var helloWorld = new ProducerRecord<>(TOPIC, i, msg + " " + i);

      var callback = new CustomCallback(i);
      producer.send(helloWorld, callback);

    }
    Thread.sleep(3000L);
  }
}