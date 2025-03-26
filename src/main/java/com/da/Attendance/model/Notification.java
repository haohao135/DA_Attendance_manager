package com.da.Attendance.model;

import com.da.Attendance.model.enums.NotificationType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document
public class Notification {
    @Id
    private String id;
    private String title;
    private String content;
    private String senderId;
    private String receivedId;
    private NotificationType type;
    private Instant createAt = Instant.now();
}
