package org.example;

import com.github.javafaker.Faker;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutionException;

/*
  config 설정 정보 readme.md 참고
 */

public class IdempotencePizzaProducer {

  public static final String CONFLUENT_BROKER = System.getenv("CONFLUENT_BROKER");
  public static final String TOPIC = "pizza-topic";
  private static final Logger logger = LoggerFactory.getLogger(
    IdempotencePizzaProducer.class.getName());

  public static void sendPizzaMessage(KafkaProducer<String, String> producer, String topicName,
    int iterCount, int interIntervalMillis, int intervalMillis, int intervalCount,
    boolean isSync) {

    PizzaMessage pizzaMessage = new PizzaMessage();
    int iterSeq = 0;

    long seed = 2023;
    var random = new Random(seed);
    var faker = Faker.instance(random);

    while (iterCount != iterSeq++) {
      var pMessage = pizzaMessage.produce_msg(faker, random, iterSeq);
      var producerRecord = new ProducerRecord<>(topicName,
        pMessage.get("key"), pMessage.get("message"));

      sendMessage(producer, producerRecord, pMessage, isSync);
      // break time
      if (intervalCount > 0 && (iterSeq % intervalCount) == 0) {
        try {
          logger.info("####### IntervalCount: {}, IntervalMillis : {} #######",
            intervalCount, intervalMillis);
          Thread.sleep(intervalMillis);
        } catch (InterruptedException e) {
          logger.error(e.getMessage());
        }
      }

      if (interIntervalMillis > 0) {
        try {
          logger.info("interIntervalMillis: {}", interIntervalMillis);
          Thread.sleep(interIntervalMillis);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      }
    }
  }

  public static void sendMessage(KafkaProducer<String, String> kafkaProducer,
    ProducerRecord<String, String> producerRecord,
    Map<String, String> pMessage, boolean isSync) {

    if (!isSync) {
      kafkaProducer.send(producerRecord, (metadata, exception) -> {
        if (exception != null) {
          exception.printStackTrace();
        } else {
          logger.info("async message : {}, partition : {}, offset : {}"
            , pMessage.get("key"), metadata.partition(), metadata.offset());
        }
      });
    } else {
      try {
        var metadata = kafkaProducer.send(producerRecord).get();
        logger.info("async message : {}, partition: {}, offset: {}, timestamp: {}",
          pMessage.get("key"),
          metadata.partition(), metadata.offset(), metadata.timestamp());
      } catch (InterruptedException | ExecutionException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public static void main(String[] args) {
    // kafka-topics --bootstrap-server kafka-0-internal.confluent.svc.cluster.local:9092 --create --topic simple-topic --partitions 1 --replication-factor 1
    /* create send kafka message example */
    var props = new Properties();
    // bootstrap.servers, key.serializer.class, value.serializer.class
    props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, CONFLUENT_BROKER);
    props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
      StringSerializer.class.getName());
    props.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
      StringSerializer.class.getName());
    // true이고, ACKS=1인 경우 오류가 난다.
    props.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
    props.setProperty(ProducerConfig.ACKS_CONFIG, "all");

    // send to kafka message key null and value "hello world"
    var producer = new KafkaProducer<String, String>(props);

    sendPizzaMessage(producer, TOPIC, -1, 10, 100, 100,
      false);
    producer.close();
  }
}