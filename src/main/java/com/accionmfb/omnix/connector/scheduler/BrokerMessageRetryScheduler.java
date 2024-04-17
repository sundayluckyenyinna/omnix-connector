package com.accionmfb.omnix.connector.scheduler;

import com.accionmfb.omnix.connector.commons.BrokerMessageDeliveryStatus;
import com.accionmfb.omnix.connector.model.BrokerOutbox;
import com.accionmfb.omnix.connector.repository.BrokerOutboxRepository;
import com.accionmfb.omnix.connector.util.ConnectorUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.List;
import java.util.Objects;

@Slf4j
@Configuration
@EnableScheduling
@RequiredArgsConstructor
@EnableConfigurationProperties(value = BrokerOutboxRetrySchedulerProperties.class )
public class BrokerMessageRetryScheduler {

    private final BrokerOutboxRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final BrokerOutboxRetrySchedulerProperties properties;


    @Scheduled(fixedDelay = 10000, initialDelay = 1000)
    public void retryUnAcknowledgeMessageBroadCast(){
        if(properties.isEnable()) {
            List<BrokerOutbox> pendingBrokerMessages = outboxRepository.findAllByStatus(BrokerMessageDeliveryStatus.FAILED);
            if(Objects.nonNull(pendingBrokerMessages) && !pendingBrokerMessages.isEmpty()) {
                log.debug("Retry broker outbox messages. Total outbox to be retried: {}", pendingBrokerMessages.size());
                pendingBrokerMessages.forEach(this::retryBrokerMessage);
            }
        }
    }

    private void retryBrokerMessage(BrokerOutbox outbox){
        ListenableFuture<SendResult<String, String>> future;
        if(Objects.nonNull(outbox)){
            if(Objects.nonNull(outbox.getMessageKey())){
                future = kafkaTemplate.send(outbox.getTopic(), outbox.getMessageKey(), outbox.getPayload());
            }else{
                future = kafkaTemplate.send(outbox.getTopic(), outbox.getPayload());
            }
            future.addCallback(new ListenableFutureCallback<>() {
                @Override
                public void onFailure(Throwable throwable) {
                    outbox.setUpdatedAt(ConnectorUtils.getCurrentDateTime());
                    outboxRepository.updateBrokerOutbox(outbox);
                }

                @Override
                public void onSuccess(SendResult<String, String> stringStringSendResult) {
                    try {
                        outboxRepository.deleteBrokerOutbox(outbox);
                    } catch (Exception ex) {
                        log.error("Error while trying to removed successful outbox record. Exception message is: {}", ex.getMessage());
                    }
                }
            });
        }
    }

}
