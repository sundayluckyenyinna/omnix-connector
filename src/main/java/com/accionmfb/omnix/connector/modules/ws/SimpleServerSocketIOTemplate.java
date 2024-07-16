package com.accionmfb.omnix.connector.modules.ws;

import com.accionmfb.omnix.connector.modules.ws.annotation.WebSocketIgnore;
import com.corundumstudio.socketio.SocketIOServer;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class SimpleServerSocketIOTemplate implements ServerSocketIOTemplate{

    private final ObjectMapper objectMapper;
    private final SocketIOServer socketIOServer;

    @Async
    @Override
    public void publishSimpleEvent(String eventName, Object data){
        try {
            String dataJson = getPayloadJson(data);
            log.info("Sending broadcast messages");
            socketIOServer.getBroadcastOperations().sendEvent(eventName, dataJson);
            log.info("Broadcast messages sent");
        }catch (Exception ignored){}
    }

    @Async
    @Override
    public void publishSimpleEvent(String room, String eventName, Object data){
        try{
            String dataJson = getPayloadJson(data);
            log.info("Sending private room message to room: {}", room);
            socketIOServer.getRoomOperations(room).sendEvent(eventName, dataJson);
            log.info("Private room message sent successfully");
        }catch (Exception ignored){}
    }

    @Async
    @Override
    public void publishSimpleEvent(List<String> rooms, String eventName, Object data){
        try{
            String dataJson = getPayloadJson(data);
            log.info("Sending private messages to multiple rooms");
            socketIOServer.getRoomOperations(rooms.toArray(new String[0])).sendEvent(eventName, dataJson);
            log.info("Message sent to multiple rooms successfully");
        }catch (Exception ignored){}
    }

    @SneakyThrows
    private String getPayloadJson(Object data) {
        String dataJson;
        if (Objects.nonNull(data)) {
            dataJson = data instanceof String ? (String) data : objectMapper.writeValueAsString(data);
        } else {
            dataJson = "null";
        }
        return dataJson;
    }
}
