package com.da.Attendance.controller;

import com.da.Attendance.dto.request.Notification.SendNotificationRequest;
import com.da.Attendance.dto.response.ApiResponse;
import com.da.Attendance.dto.response.Notification.GroupedNotificationResponse;
import com.da.Attendance.model.Notification;
import com.da.Attendance.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin("*")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
    @PostMapping("/send")
    public ResponseEntity<ApiResponse> sendNotify(@RequestBody SendNotificationRequest sendNotificationRequest){
        try{
            Notification notificationList = notificationService.sendBulkNotification(sendNotificationRequest);
            return ResponseEntity.ok(new ApiResponse("Send notification success", notificationList));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Send notification failed " + e.getMessage(), null));
        }
    }
    @GetMapping("/user")
    public ResponseEntity<ApiResponse> getNotify(@RequestParam String userId){
        try{
            List<Notification> notificationList = notificationService.getNotificationsByUserId(userId);
            return ResponseEntity.ok(new ApiResponse("Get notification success", notificationList));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Get notification failed " + e.getMessage(), null));
        }
    }
    @GetMapping("/user/starred")
    public ResponseEntity<ApiResponse> getStarredNotifications(@RequestParam String userId){
        try{
            List<Notification> notificationList = notificationService.getStarredNotificationsByReceiver(userId);
            return ResponseEntity.ok(new ApiResponse("Get notification success", notificationList));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Get notification failed " + e.getMessage(), null));
        }
    }
    @PostMapping("/{id}/read")
    public ResponseEntity<ApiResponse> markAsRead(@PathVariable String id){
        try{
            Notification notification = notificationService.changeIsRead(id);
            return ResponseEntity.ok(new ApiResponse("Change notification success", notification));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Change notification failed " + e.getMessage(), null));
        }
    }
    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse> getAll(){
        try{
            List<Notification> notificationList = notificationService.getAll();
            return ResponseEntity.ok(new ApiResponse("Get notifications success", notificationList));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Get notifications failed " + e.getMessage(), null));
        }
    }
    @GetMapping("/grouped")
    public ResponseEntity<ApiResponse> getGroupedNotifications(@RequestParam String senderId){
        try{
            List<GroupedNotificationResponse> notificationList =
                    notificationService.getGroupedNotifications(senderId);
            return ResponseEntity.ok(new ApiResponse("Get notifications success", notificationList));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Get notifications failed " + e.getMessage(), null));
        }
    }
    @GetMapping("/grouped/starred")
    public ResponseEntity<ApiResponse> getGroupedStarredNotifications(@RequestParam String senderId){
        try{
            List<GroupedNotificationResponse> notificationList =
                    notificationService.getGroupedStarredNotifications(senderId);
            return ResponseEntity.ok(new ApiResponse("Get notifications success", notificationList));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Get notifications failed " + e.getMessage(), null));
        }
    }
    @PostMapping("/update/starred")
    public ResponseEntity<ApiResponse> updateStarredStatus(@RequestParam String id,
                                                           @RequestParam boolean isStarred){
        try{
            Notification notification = notificationService.updateStarredStatus(id, isStarred);
            return ResponseEntity.ok(new ApiResponse("Update starred notifications success", notification));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Update starred notifications failed " + e.getMessage(), null));
        }
    }
    @PostMapping("/update/starred/sender")
    public ResponseEntity<ApiResponse> updateStarredBySender(@RequestParam String id,
                                                             @RequestParam boolean isStarred){
        try{
            Notification notification = notificationService.updateStarredBySender(id, isStarred);
            return ResponseEntity.ok(new ApiResponse("Update starred notifications success", notification));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Update starred notifications failed " + e.getMessage(), null));
        }
    }
}
