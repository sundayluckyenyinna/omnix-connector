package com.accionmfb.omnix.connector.modules.nats;

import com.accionmfb.omnix.connector.commons.Broker;
import com.accionmfb.omnix.connector.commons.BrokerMessageDeliveryStatus;
import com.accionmfb.omnix.connector.model.BrokerOutbox;
import com.accionmfb.omnix.connector.modules.OmnixStreamPayload;
import com.accionmfb.omnix.connector.repository.BrokerOutboxRepository;
import com.accionmfb.omnix.connector.util.FallbackBrokerOperation;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.nats.client.Connection;
import io.nats.client.Message;
import io.nats.client.impl.NatsMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static com.accionmfb.omnix.connector.util.ConnectorUtils.getCurrentDateTime;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DefaultNatsService implements NatsService {

    private final Connection connection;
    private final ObjectMapper objectMapper;
    private final BrokerOutboxRepository outboxRepository;


    @Override
    public void publishMessage(Object subject, OmnixStreamPayload<?> omnixStreamPayload){
        try {
           sendAndCompleteSuccessMessage(subject, omnixStreamPayload);
        }catch (Exception exception){
            log.error("Exception occurred while trying to publish messages with the NATS broker. Exception message is: {}", exception.getMessage());
        }
    }

    @Override
    public void publishMessageWithFallback(Object subject, OmnixStreamPayload<?> omnixStreamPayload, FallbackBrokerOperation fallbackBrokerOperation){
        try {
            sendAndCompleteSuccessMessage(subject, omnixStreamPayload);
        }catch (Exception exception){
            log.error("Exception occurred while trying to publish messages with the NATS broker. Exception message is: {}", exception.getMessage());
            if(Objects.nonNull(fallbackBrokerOperation)) {
                log.info("The fall back operation will be called");
                fallbackBrokerOperation.runFallbackOperation();
            }
        }
    }


    @SneakyThrows
    private void sendAndCompleteSuccessMessage(Object subject, OmnixStreamPayload<?> omnixStreamPayload){
        String subjectStr = String.valueOf(subject);
        String dataJson = objectMapper.writeValueAsString(omnixStreamPayload);
        BrokerOutbox createdOutbox = buildOutbox(subjectStr, dataJson);
        outboxRepository.saveAndFlush(createdOutbox);
        Message message = NatsMessage.builder()
                .data(dataJson, StandardCharsets.UTF_8)
                .subject(subjectStr)
                .build();
        connection.publish(message);
        completeNatsSuccessAction(createdOutbox);
    }

    public void completeNatsSuccessAction(BrokerOutbox createdOutbox) {
        System.out.println();
        log.info("-------------------------------------------------------------------------------------------------------------------------------------------");
        log.info("Nats broker acknowledge the message successfully to the subject '{}'", createdOutbox.getTopic());
        try{
            log.info("Deleting successfully sent message from outbox table");
            outboxRepository.deleteBrokerOutbox(createdOutbox);
            log.info("Message deleted successfully");
            log.info("-------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.println();
        } catch (Exception ex) {
            log.error("Error while trying to removed successful outbox record. Exception message is: {}", ex.getMessage());
        }
    }

    private BrokerOutbox buildOutbox(String subject, String payloadJson){
        return BrokerOutbox.builder()
                .broker(Broker.NATS)
                .status(BrokerMessageDeliveryStatus.PENDING)
                .createdAt(getCurrentDateTime())
                .updatedAt(getCurrentDateTime())
                .topic(subject)
                .messageKey(Strings.EMPTY)
                .payload(payloadJson)
                .build();
    }

    private void logAcknowledgement(String subject){
        log.info("-------------------------------------------------------------------------------------------------------------------------------------------");
        log.info("Nats broker acknowledge the message successfully to the subject '{}'", subject);
        log.info("-------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.println();
    }

}
