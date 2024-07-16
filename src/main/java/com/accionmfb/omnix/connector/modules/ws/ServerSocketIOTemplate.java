package com.accionmfb.omnix.connector.modules.ws;

import org.springframework.scheduling.annotation.Async;

import java.util.List;

public interface ServerSocketIOTemplate {

    void publishSimpleEvent(String eventName, Object data);

    @Async
    void publishSimpleEvent(String room, String eventName, Object data);

    @Async
    void publishSimpleEvent(List<String> rooms, String eventName, Object data);
}
