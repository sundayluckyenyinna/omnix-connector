package com.accionmfb.omnix.connector.modules.kafka;

import com.accionmfb.omnix.connector.commons.BrokerMessageDeliveryStatus;
import com.accionmfb.omnix.connector.model.BrokerOutbox;
import com.accionmfb.omnix.connector.repository.BrokerOutboxRepository;
import com.accionmfb.omnix.connector.util.FallbackKafkaOperation;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import static com.accionmfb.omnix.connector.util.ConnectorUtils.getCurrentDateTime;
import static com.accionmfb.omnix.connector.util.ConnectorUtils.returnOrDefault;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DefaultKafkaBrokerService implements KafkaService{

    private final ObjectMapper objectMapper;
    private final BrokerOutboxRepository repository;
    private final KafkaTemplate<String, String> kafkaTemplate;


    @Override
    @SneakyThrows
    public void publishMessage(Object topic, OmnixKafkaPayload omnixKafkaPayload){
        String payloadJson = objectMapper.writeValueAsString(omnixKafkaPayload);
        String topicStr = String.valueOf(topic);
        BrokerOutbox outbox = buildOutbox(topicStr, null, payloadJson);
        BrokerOutbox createdOutbox = repository.saveAndFlush(outbox);
        ListenableFuture<SendResult<String, String>> future = publishMessageWithoutOutbox(topicStr, payloadJson);
        completeOutboxFuture(future, createdOutbox);
    }

    @Override
    @SneakyThrows
    public void publishMessage(Object topic, String key, OmnixKafkaPayload omnixKafkaPayload){
        String payloadJson = objectMapper.writeValueAsString(omnixKafkaPayload);
        String topicStr = String.valueOf(topic);
        BrokerOutbox outbox = buildOutbox(topicStr, key, payloadJson);
        BrokerOutbox createdOutbox = repository.saveAndFlush(outbox);
        ListenableFuture<SendResult<String, String>> future = publishMessageWithoutOutbox(topicStr, payloadJson);
        completeOutboxFuture(future, createdOutbox);
    }

    @Override
    @SneakyThrows
    public void publishMessageWithFallback(Object topic, OmnixKafkaPayload omnixKafkaPayload, FallbackKafkaOperation operation){
        String payloadJson = objectMapper.writeValueAsString(omnixKafkaPayload);
        String topicStr = String.valueOf(topic);
        ListenableFuture<SendResult<String, String>> future = publishMessageWithoutOutbox(topicStr, payloadJson);
        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable exception) {
                System.out.println();
                log.info("-------------------------------------------------------------------------------------------------------------------------------------------");
                log.error("Kafka failed to deliver message to the topic: '{}'. Exception message is: {}", topic, exception.getMessage());
                log.warn("Kafka broker did not acknowledge/establish handshake. The default operation will be executed");
                log.info("-------------------------------------------------------------------------------------------------------------------------------------------");
                System.out.println();
                operation.runFallbackOperation();
            }

            @Override
            public void onSuccess(SendResult<String, String> stringStringSendResult) {
                System.out.println();
                log.info("-------------------------------------------------------------------------------------------------------------------------------------------");
                log.info("Kafka broker acknowledge the message successfully to the topic '{}'", topic);
                log.info("-------------------------------------------------------------------------------------------------------------------------------------------");
                System.out.println();
            }
        });
    }

    @Override
    @SneakyThrows
    public void publishMessageWithFallback(Object topic, String key, OmnixKafkaPayload omnixKafkaPayload, FallbackKafkaOperation operation){
        String payloadJson = objectMapper.writeValueAsString(omnixKafkaPayload);
        String topicStr = String.valueOf(topic);
        ListenableFuture<SendResult<String, String>> future = publishMessageWithoutOutbox(topicStr, payloadJson);
        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable exception) {
                System.out.println();
                log.info("-------------------------------------------------------------------------------------------------------------------------------------------");
                log.error("Kafka failed to deliver message to the topic: '{}' and message key: '{}'. Exception message is: {}", topic, key, exception.getMessage());
                log.warn("Kafka broker did not acknowledge/establish handshake. The default operation will be executed");
                log.info("-------------------------------------------------------------------------------------------------------------------------------------------");
                System.out.println();
                operation.runFallbackOperation();
            }

            @Override
            public void onSuccess(SendResult<String, String> stringStringSendResult) {
                System.out.println();
                log.info("-------------------------------------------------------------------------------------------------------------------------------------------");
                log.info("Kafka broker acknowledge the message successfully to the topic '{}' and message key: '{}'", topic, key);
                log.info("-------------------------------------------------------------------------------------------------------------------------------------------");
                System.out.println();
            }
        });
    }

    private ListenableFuture<SendResult<String, String>> publishMessageWithoutOutbox(String topic, String payloadJson){
        return kafkaTemplate.send(topic, payloadJson);
    }

    public void completeOutboxFuture(ListenableFuture<SendResult<String, String>> future, BrokerOutbox createdOutbox){
        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable exception) {
                System.out.println();
                log.info("-------------------------------------------------------------------------------------------------------------------------------------------");
                log.error("Kafka failed to deliver message to the topic: '{}'. Exception message is: {}", createdOutbox.getTopic(), exception.getMessage());
                log.warn("Kafka broker did not acknowledge/establish handshake. The default operation will be executed");
                log.warn("Updating status of the outbox to 'FAILED'");
                createdOutbox.setStatus(BrokerMessageDeliveryStatus.FAILED);
                createdOutbox.setUpdatedAt(getCurrentDateTime());
                repository.updateBrokerOutbox(createdOutbox);
                log.info("Message outbox updated to 'FAILED'");
                log.info("-------------------------------------------------------------------------------------------------------------------------------------------");
                System.out.println();
            }

            @Override
            public void onSuccess(SendResult<String, String> stringStringSendResult) {
                System.out.println();
                log.info("-------------------------------------------------------------------------------------------------------------------------------------------");
                log.info("Kafka broker acknowledge the message successfully to the topic '{}' and message key: '{}'", createdOutbox.getTopic(), createdOutbox.getMessageKey());
                try {
                    log.info("Deleting successfully sent message from outbox table");
                    repository.deleteBrokerOutbox(createdOutbox);
                    log.info("Message deleted successfully");
                    log.info("-------------------------------------------------------------------------------------------------------------------------------------------");
                    System.out.println();
                } catch (Exception ex) {
                    log.error("Error while trying to removed successful outbox record. Exception message is: {}", ex.getMessage());
                }
            }
        });
    }

    private BrokerOutbox buildOutbox(String topic, String key, String payloadJson){
        return BrokerOutbox.builder()
                .status(BrokerMessageDeliveryStatus.PENDING)
                .createdAt(getCurrentDateTime())
                .updatedAt(getCurrentDateTime())
                .topic(topic)
                .messageKey(returnOrDefault(key, Strings.EMPTY))
                .payload(payloadJson)
                .build();
    }
}
