package com.da.Attendance.repository;

import com.da.Attendance.model.Event;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface EventRepository extends MongoRepository<Event, String> {
    List<Event> findByParticipantIdsContaining(String participantId);
    List<Event> findByOrganizerId(String organizerId);
    List<Event> findByOrganizerIdAndTimeStartAfter(String organizerId, Instant now, Sort sort);
    List<Event> findByOrganizerIdAndTimeEndBefore(String organizerId, Instant now, Sort sort);
}
