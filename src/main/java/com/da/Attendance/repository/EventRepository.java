package com.da.Attendance.repository;

import com.da.Attendance.model.Event;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends MongoRepository<Event, String> {
    List<Event> findByParticipantIdsContaining(String participantId);
}
