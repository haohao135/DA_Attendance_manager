package com.da.Attendance.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Document
public class Event {
    @Id
    private String id;
    private String nameEvent;
    private String description;
    private String location;
    private Instant timeStart;
    private Instant timeEnd;
    private String organizerId;
    private List<String> participantIds = new ArrayList<>();

    public Event() {
    }

    public Event(String nameEvent, String description, String location, Instant timeStart, Instant timeEnd, String organizerId) {
        this.nameEvent = nameEvent;
        this.description = description;
        this.location = location;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.organizerId = organizerId;
    }

    public Event(String id, String nameEvent, String description, String location, Instant timeStart, Instant timeEnd, String organizerId) {
        this.id = id;
        this.nameEvent = nameEvent;
        this.description = description;
        this.location = location;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.organizerId = organizerId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameEvent() {
        return nameEvent;
    }

    public void setNameEvent(String nameEvent) {
        this.nameEvent = nameEvent;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(String organizerId) {
        this.organizerId = organizerId;
    }

    public List<String> getParticipantIds() {
        return participantIds;
    }

    public Instant getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Instant timeStart) {
        this.timeStart = timeStart;
    }

    public Instant getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Instant timeEnd) {
        this.timeEnd = timeEnd;
    }

    public void setParticipantIds(List<String> participantIds) {
        this.participantIds = participantIds;
    }
}
