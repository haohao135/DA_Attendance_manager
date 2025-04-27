package com.da.Attendance.service.Imp;

import com.corundumstudio.socketio.SocketIOServer;
import com.da.Attendance.dto.request.Notification.SendNotificationRequest;
import com.da.Attendance.model.Notification;
import com.da.Attendance.model.enums.NotificationType;
import com.da.Attendance.repository.NotificationRepository;
import com.da.Attendance.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class NotificationServiceImp implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private SocketIOServer socketIOServer;

    @Override
    public List<Notification> sendBulkNotification(SendNotificationRequest request) {
        if (request.getStudentIds() == null || request.getStudentIds().isEmpty()) {
            throw new IllegalArgumentException("student list cannot be null or empty.");
        }
        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new IllegalArgumentException("title cannot be null or empty.");
        }
        if (request.getContent() == null || request.getContent().isBlank()) {
            throw new IllegalArgumentException("content cannot be null or empty.");
        }
        if (request.getSenderId() == null) {
            throw new IllegalArgumentException("sender ID cannot be null.");
        }
        if (!EnumSet.allOf(NotificationType.class).contains(request.getType())) {
            throw new IllegalArgumentException("invalid notification type.");
        }

        List<Notification> notifications = new ArrayList<>();
        for (String studentId : request.getStudentIds()) {
            Notification notification = new Notification();
            notification.setTitle(request.getTitle());
            notification.setContent(request.getContent());
            notification.setSenderId(request.getSenderId());
            notification.setReceivedId(studentId);
            notification.setType(request.getType());
            notification.setCreateAt(Instant.now());
            notification.setRead(false);
            try {
                socketIOServer.getRoomOperations(studentId).sendEvent("notification", notification);
            } catch (Exception e) {
                throw new RuntimeException("failed to send notifications", e);
            }
            notifications.add(notification);
        }
        try {
            return notificationRepository.saveAll(notifications);
        } catch (Exception e) {
            throw new RuntimeException("failed to save notifications", e);
        }
    }

    @Override
    public List<Notification> getNotificationsByUserId(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("user ID cannot be null or empty.");
        }
        return notificationRepository.findByReceivedIdOrderByCreateAtDesc(userId);
    }

    @Override
    public Notification getById(String id) {
        Optional<Notification> notification = notificationRepository.findById(id);
        if(notification.isEmpty()){
            throw new RuntimeException("notification not found");
        }
        return notification.get();
    }

    @Override
    public Notification changeIsRead(String id) {
        Notification notification = getById(id);
        notification.setRead(true);
        return notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getAll() {
        return notificationRepository.findAll();
    }
}