package com.da.Attendance.websocket;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.net.URI;
import java.security.Principal;
import java.util.Map;

public class UserHandshakeHandler extends DefaultHandshakeHandler {
    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        URI uri = request.getURI();
        String query = uri.getQuery(); // userId=abc123
        String userId = null;

        if (query != null && query.contains("userId=")) {
            for (String param : query.split("&")) {
                if (param.startsWith("userId=")) {
                    userId = param.substring("userId=".length());
                    break;
                }
            }
        }
        if (userId == null || userId.isBlank()) {
            return null;
        }

        String finalUserId = userId;
        return () -> finalUserId;
    }
}
