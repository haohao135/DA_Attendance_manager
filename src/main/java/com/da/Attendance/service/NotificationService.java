package com.da.Attendance.service;

import com.da.Attendance.dto.request.Notification.SendNotificationRequest;
import com.da.Attendance.model.Notification;

import java.util.List;

public interface NotificationService {
    List<Notification> sendBulkNotification(SendNotificationRequest sendNotificationRequest);
    List<Notification> getNotificationsByUserId(String userId);
    Notification getById(String id);
    Notification changeIsRead(String id);
    List<Notification> getAll();
}
