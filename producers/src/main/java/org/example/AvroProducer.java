package org.example;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;

public class AvroProducer {
  public static final String CONFLUENT_BROKER = System.getenv("CONFLUENT_BROKER");

  private static final String TOPIC_NAME = "http-logs";
    private static final String KAFKA_BROKERS = CONFLUENT_BROKER;
    private static final String AVRO_SCHEMA = "{\n"
      + "  \"doc\": \"Sample schema to help you get started.\",\n"
      + "  \"fields\": [\n"
      + "    {\n"
      + "      \"doc\": \"The int type is a 32-bit signed integer.\",\n"
      + "      \"name\": \"my_field1\",\n"
      + "      \"type\": \"int\"\n"
      + "    },\n"
      + "    {\n"
      + "      \"doc\": \"The double type is a double precision (64-bit) IEEE 754 floating-point number.\",\n"
      + "      \"name\": \"my_field2\",\n"
      + "      \"type\": \"double\"\n"
      + "    },\n"
      + "    {\n"
      + "      \"doc\": \"The string is a unicode character sequence.\",\n"
      + "      \"name\": \"my_field3\",\n"
      + "      \"type\": \"string\"\n"
      + "    }\n"
      + "  ],\n"
      + "  \"name\": \"sampleRecord\",\n"
      + "  \"namespace\": \"fluent\",\n"
      + "  \"type\": \"record\"\n"
      + "}";

    public static void main(String[] args) throws IOException {
        // Avro schema 파싱
        Schema.Parser parser = new Schema.Parser();
        Schema schema = parser.parse(AVRO_SCHEMA);

        // Kafka producer 설정
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_BROKERS);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());

        Producer<String, byte[]> producer = new KafkaProducer<>(props);

        // 메시지 생성
        GenericRecord message = new GenericData.Record(schema);

        message.put("my_field1", 123);
        message.put("my_field2", 123.9321);
        message.put("my_field3", "example");

        // 메시지 직렬화
        DatumWriter<GenericRecord> writer = new SpecificDatumWriter<>(schema);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(outputStream, null);
        writer.write(message, encoder);
        encoder.flush();

        // 메시지 전송
        ProducerRecord<String, byte[]> record = new ProducerRecord<>(TOPIC_NAME, outputStream.toByteArray());
        producer.send(record);

        producer.flush();
        producer.close();
    }
}
