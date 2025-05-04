package com.da.Attendance.dto.response.Notification;

import com.da.Attendance.model.enums.NotificationType;

import java.time.Instant;

public class GroupedNotificationResponse {
    private String title;
    private String content;
    private String senderId;
    private NotificationType type;
    private Instant createAt;
    private long totalSent;

    public GroupedNotificationResponse() {
    }

    public GroupedNotificationResponse(String title, String content, String senderId, NotificationType type, Instant createAt, long totalSent) {
        this.title = title;
        this.content = content;
        this.senderId = senderId;
        this.type = type;
        this.createAt = createAt;
        this.totalSent = totalSent;
    }

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

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public Instant getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Instant createAt) {
        this.createAt = createAt;
    }

    public long getTotalSent() {
        return totalSent;
    }

    public void setTotalSent(long totalSent) {
        this.totalSent = totalSent;
    }
}
