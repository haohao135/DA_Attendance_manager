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
    private boolean isRead = false;
    private boolean isStarred = false;
    private boolean isStarredBySender = false;

    public Notification() {
    }

    public Notification(String id, String title, String content, String senderId, String receivedId, NotificationType type, Instant createAt, boolean isRead, boolean isStarred, boolean isStarredBySender) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.senderId = senderId;
        this.receivedId = receivedId;
        this.type = type;
        this.createAt = createAt;
        this.isRead = isRead;
        this.isStarred = isStarred;
        this.isStarredBySender = isStarredBySender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getReceivedId() {
        return receivedId;
    }

    public void setReceivedId(String receivedId) {
        this.receivedId = receivedId;
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

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public boolean isStarred() {
        return isStarred;
    }

    public void setStarred(boolean starred) {
        isStarred = starred;
    }

    public boolean isStarredBySender() {
        return isStarredBySender;
    }

    public void setStarredBySender(boolean starredBySender) {
        isStarredBySender = starredBySender;
    }
}
