package com.accionmfb.omnix.connector.modules;

import lombok.*;

@Data
@ToString
@NoArgsConstructor
public class OmnixStreamPayload<T> {
    private T payload;
    private String authToken;
    private String idToken;

    public OmnixStreamPayload(T payload, String authToken, String idToken){
        this.payload = payload;
        this.authToken = authToken;
        this.idToken = idToken;
    }

    public static <T> OmnixStreamPayload<T> from(T payload, String authToken, String idToken){
        return new OmnixStreamPayload<>(payload, authToken, idToken);
    }

    public static <T> OmnixStreamPayload<T> from(T payload, String authToken){
        return new OmnixStreamPayload<>(payload, authToken, null);
    }
}
