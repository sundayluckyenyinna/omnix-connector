package com.accionmfb.omnix.connector.modules.kafka;

import lombok.*;

@Data
@ToString
@NoArgsConstructor
public class OmnixKafkaPayload<T> {
    private T payload;
    private String authToken;
    private String idToken;

    public OmnixKafkaPayload(T payload, String authToken, String idToken){
        this.payload = payload;
        this.authToken = authToken;
        this.idToken = idToken;
    }

    public static <T> OmnixKafkaPayload<T> from(T payload, String authToken, String idToken){
        return new OmnixKafkaPayload<>(payload, authToken, idToken);
    }

    public static <T> OmnixKafkaPayload<T> from(T payload, String authToken){
        return new OmnixKafkaPayload<>(payload, authToken, null);
    }
}
