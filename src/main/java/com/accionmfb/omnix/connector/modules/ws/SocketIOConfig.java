package com.accionmfb.omnix.connector.modules.ws;

import com.corundumstudio.socketio.SocketIOServer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(ServerSocketIOConfigurationProperties.class)
public class SocketIOConfig {

    private final ServerSocketIOConfigurationProperties configurationProperties;

    @Bean
    public SocketIOServer getWsServerInstance(){
        if(configurationProperties.isExpose()) {
            String host = configurationProperties.getHostName();
            Integer port = configurationProperties.getPort();
            com.corundumstudio.socketio.Configuration configuration = getServerSocketConfiguration(host, port);
            final SocketIOServer socketIOServer = new SocketIOServer(configuration);
            socketIOServer.addConnectListener(socketIOClient -> log.info("New Client Connected with SessionID: {}", socketIOClient.getSessionId()));
            socketIOServer.addDisconnectListener(socketIOClient -> log.info("Client Disconnected with SessionID: {}", socketIOClient.getSessionId()));
            try {
                socketIOServer.start();
                logServerSocketConnectionSuccessMessage();
            } catch (Exception exception) {
                logServerSocketConnectionErrorMessage(exception);
            }
            return socketIOServer;
        }
        return new SocketIOServer(new com.corundumstudio.socketio.Configuration());
    }

    private static com.corundumstudio.socketio.Configuration getServerSocketConfiguration(String host, Integer port){
        com.corundumstudio.socketio.Configuration configuration = new com.corundumstudio.socketio.Configuration();
        configuration.setHostname(host);
        configuration.setPort(port);
        return configuration;
    }

    private static void logServerSocketConnectionSuccessMessage(){
        try{
            System.out.println();
            log.info("-----------------------------------------------------------------------------------");
            log.info("\u001B[32m WEBSOCKET CONNECTION ESTABLISHED AND READY FOR BIDIRECTIONAL INTERACTION\u001B[0m");
            log.info("-----------------------------------------------------------------------------------");
            System.out.println();
        }catch (Exception ignored){}
    }

    private static void logServerSocketConnectionErrorMessage(Exception exception){
        try {
            log.error("\u001B[31m{}\u001B[0m", "WEBSOCKET PROVISIONING FAILED TO ESTABLISH");
            log.error("Websocket provisioning exception message is: {}", exception.getMessage());
        }catch (Exception ignored){}
    }
}
