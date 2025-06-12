package com.da.Attendance.service;

import com.da.Attendance.dto.request.Event.AddEventRequest;
import com.da.Attendance.dto.request.Event.AddStudentsRequest;
import com.da.Attendance.dto.request.Event.UpdateEventRequest;
import com.da.Attendance.model.Event;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.List;

public interface EventService {
    Event findEventById(String id);
    Event addEvent(AddEventRequest addEventRequest);
    Event addStudent(String id, String studentId);
    Event addStudentList(AddStudentsRequest addStudentsRequest);
    Event updateEvent(String id, UpdateEventRequest updateEventRequest);
    Event updateTime(String id, Instant instant);
    List<Event> getEventByStudentId(String id);
    void deleteEvent(String id);
    List<Event> getAllEventsByOrganizerId(String organizerId);
    List<Event> getUpcomingEventsByOrganizerId(String organizerId);
    List<Event> getPastEventsByOrganizerId(String organizerId);
    List<Event> getAllEvent();
    ByteArrayInputStream exportEventAttendanceReport(String eventId) throws IOException;
    void removeStudent(String id, String studentId);
    void importStudentsFromCSV(String eventId, MultipartFile file) throws IOException;
}
