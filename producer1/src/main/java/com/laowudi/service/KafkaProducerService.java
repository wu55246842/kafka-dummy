package com.laowudi.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private RetryTemplate retryTemplate;

    public void sendMessage(String topic, String message) {
        retryTemplate.execute(context -> {
            ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, message);
            future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {

                @Override
                public void onSuccess(SendResult<String, Object> result) {
                    System.out.println("Sent message=[" + message +
                            "] with offset=[" + result.getRecordMetadata().offset() + "]");
                }

                @Override
                public void onFailure(Throwable ex) {
                    System.out.println("Unable to send message=["
                            + message + "] due to : " + ex.getMessage());
                    // Handle failure: message will be sent to dead-letter topic by the recovery callback in KafkaTemplate
                }
            });
            return null;
        }, context -> {
            // If retries fail, send the message to the dead-letter topic
            sendToDeadLetterQueue(topic, message);
            return null;
        });
    }

    private void sendToDeadLetterQueue(String topic, Object message) {
        kafkaTemplate.send("dead-letter-topic", topic, message);
    }
}
