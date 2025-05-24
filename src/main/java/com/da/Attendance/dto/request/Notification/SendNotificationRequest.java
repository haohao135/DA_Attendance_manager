package com.da.Attendance.dto.request.Notification;

import com.da.Attendance.model.enums.NotificationType;

import java.util.List;

public class SendNotificationRequest {
    private String title;
    private String content;
    private String senderId;
    private String studentId;
    private NotificationType type;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public SendNotificationRequest() {
    }

    public SendNotificationRequest(String title, String content, String senderId, String studentId, NotificationType type) {
        this.title = title;
        this.content = content;
        this.senderId = senderId;
        this.studentId = studentId;
        this.type = type;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }
}
