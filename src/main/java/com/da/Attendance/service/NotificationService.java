package com.da.Attendance.service;

import com.da.Attendance.dto.request.Notification.SendNotificationRequest;
import com.da.Attendance.dto.response.Notification.GroupedNotificationResponse;
import com.da.Attendance.model.Notification;

import java.util.List;

public interface NotificationService {
    List<Notification> sendBulkNotification(SendNotificationRequest sendNotificationRequest);
    List<Notification> getNotificationsByUserId(String userId);
    Notification getById(String id);
    Notification changeIsRead(String id);
    List<Notification> getAll();
    List<GroupedNotificationResponse> getGroupedNotifications(String senderId);
    List<GroupedNotificationResponse> getGroupedStarredNotifications(String senderId);
    List<Notification> getStarredNotificationsByReceiver(String receiverId);
    Notification updateStarredStatus(String notificationId, boolean isStarred);
    Notification updateStarredBySender(String notificationId, boolean isStarred);
}
