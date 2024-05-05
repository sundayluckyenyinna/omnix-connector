package com.accionmfb.omnix.connector.model;

import com.accionmfb.omnix.connector.commons.Broker;
import com.accionmfb.omnix.connector.commons.BrokerMessageDeliveryStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrokerOutbox {

    private Long id;
    private String topic;
    private String messageKey;
    private String payload;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Broker broker;
    private BrokerMessageDeliveryStatus status = BrokerMessageDeliveryStatus.PENDING;
}
