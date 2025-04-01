package com.da.Attendance.service;

import com.da.Attendance.dto.request.Event.AddEventRequest;
import com.da.Attendance.model.Event;

public interface EventService {
    Event addEvent(AddEventRequest addEventRequest);
}
