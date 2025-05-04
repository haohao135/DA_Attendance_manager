package com.da.Attendance.repository;

import com.da.Attendance.model.Notification;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findBySenderIdOrderByCreateAtDesc(String senderId);
    List<Notification> findByReceivedIdAndIsStarredTrueOrderByCreateAtDesc(String receivedId);
    List<Notification> findBySenderIdAndIsStarredBySenderTrueOrderByCreateAtDesc(String senderId);
    List<Notification> findByReceivedIdOrderByCreateAtDesc(String receivedId);
}
