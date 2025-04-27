package com.da.Attendance.config;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;


@org.springframework.context.annotation.Configuration
public class SocketIOConfig {
    @Bean
    public SocketIOServer socketIOServer() {
        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(9092);
        config.setOrigin("*");

        return new SocketIOServer(config);
    }

    @Bean
    public ApplicationRunner applicationRunner(SocketIOServer server) {
        return args -> server.start();
    }
}
