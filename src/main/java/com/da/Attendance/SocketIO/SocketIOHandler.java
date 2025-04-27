package com.da.Attendance.SocketIO;

import com.corundumstudio.socketio.SocketIOServer;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SocketIOHandler {
    @Autowired
    private SocketIOServer server;
    @PostConstruct
    private void init() {
        server.addConnectListener(client -> {
            String userId = client.getHandshakeData().getSingleUrlParam("userId");
            if (userId != null) {
                client.joinRoom(userId);
                System.out.println("Client joined room: " + userId);
            }
        });

        server.addDisconnectListener(client -> {
            System.out.println("Client disconnected: " + client.getSessionId());
        });
    }
}
