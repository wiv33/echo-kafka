package org.example;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomCallback implements Callback {

    public static final Logger logger = LoggerFactory.getLogger(CustomCallback.class.getName());

    private int seq;
    public CustomCallback(int seq) {
        this.seq = seq;
    }

    @Override
    public void onCompletion(RecordMetadata metadata, Exception exception) {
        if (exception != null) {
            exception.printStackTrace();
        } else {
            logger.info("topic : {}, seq : {}, partition : {}, offset : {}"
                        , metadata.topic(), seq, metadata.partition(), metadata.offset());
        }
    }
}
