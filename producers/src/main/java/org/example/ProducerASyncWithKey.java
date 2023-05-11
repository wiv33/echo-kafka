package org.example;

import java.util.concurrent.ExecutionException;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.Properties;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ProducerASyncWithKey {
  public static final String CONFLUENT_BROKER = System.getenv("CONFLUENT_BROKER");
  public static final String TOPIC = "multipart-topic";
  private static final Logger logger = LoggerFactory.getLogger(ProducerASyncWithKey.class.getName());

  public static void main(String[] args) throws InterruptedException {
    // kafka-topics --bootstrap-server kafka-0-internal.confluent.svc.cluster.local:9092 --create --topic simple-topic --partitions 1 --replication-factor 1
    /* create send kafka message example */
    var props = new Properties();
    // bootstrap.servers, key.serializer.class, value.serializer.class
    props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, CONFLUENT_BROKER);
    props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    props.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

    // send to kafka message key null and value "hello world"
    var producer = new org.apache.kafka.clients.producer.KafkaProducer<String, String>(props);

    for(int i = 0; i < 20; i++) {
      var msg = "hello world 2";
      var helloWorld = new ProducerRecord<>(TOPIC, String.valueOf(i), msg);

      try {
        producer.send(helloWorld, (metadata, exception) -> {
          if (exception != null) {
            logger.error("exception error from broker {}", exception.getMessage());
          } else {
            logger.info("\n##### record metadata received \n" +
              "Topic: " + metadata.topic() + "\n" +
              "Partition: " + metadata.partition() + "\n" +
              "Offset: " + metadata.offset() + "\n" +
              "Timestamp: " + metadata.timestamp());
          }
        }).get();

      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (ExecutionException e) {
        e.printStackTrace();
      }
    }

    Thread.sleep(3000L);

    }
}