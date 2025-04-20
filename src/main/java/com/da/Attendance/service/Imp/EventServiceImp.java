package com.da.Attendance.service.Imp;

import com.da.Attendance.dto.request.Event.AddEventRequest;
import com.da.Attendance.dto.request.Event.UpdateEventRequest;
import com.da.Attendance.model.Event;
import com.da.Attendance.repository.EventRepository;
import com.da.Attendance.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class EventServiceImp implements EventService {
    @Autowired
    private EventRepository eventRepository;

    @Override
    public Event findEventById(String id) {
        Optional<Event> events = eventRepository.findById(id);
        if(events.isEmpty()){
            throw new RuntimeException("event not found");
        }
        return events.get();
    }

    @Override
    public Event addEvent(AddEventRequest addEventRequest) {
        List<String> participantIds = new ArrayList<>();
        Event event = new Event(
                addEventRequest.getNameEvent(),
                addEventRequest.getDescription(),
                addEventRequest.getLocation(),
                addEventRequest.getDateTime(),
                addEventRequest.getOrganizerId()
        );
        return eventRepository.save(event);
    }

    @Override
    public Event updateEvent(String id, UpdateEventRequest updateEventRequest) {
        Event event = findEventById(id);
        event.setNameEvent(updateEventRequest.getNameEvent());
        event.setDescription(updateEventRequest.getDescription());
        event.setLocation(updateEventRequest.getLocation());
        return eventRepository.save(event);
    }

    @Override
    public Event updateTime(String id, Instant instant) {
        Event event = findEventById(id);
        event.setDateTime(instant);
        return eventRepository.save(event);
    }

    @Override
    public void deleteEvent(String id) {
        eventRepository.deleteById(id);
    }
}
