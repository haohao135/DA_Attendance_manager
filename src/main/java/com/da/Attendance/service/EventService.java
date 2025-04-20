package com.da.Attendance.service;

import com.da.Attendance.dto.request.Event.AddEventRequest;
import com.da.Attendance.dto.request.Event.UpdateEventRequest;
import com.da.Attendance.model.Event;

import java.time.Instant;

public interface EventService {
    Event findEventById(String id);
    Event addEvent(AddEventRequest addEventRequest);
    Event updateEvent(String id, UpdateEventRequest updateEventRequest);
    Event updateTime(String id, Instant instant);
    void deleteEvent(String id);
}
