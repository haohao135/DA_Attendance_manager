package com.da.Attendance.dto.request.Event;

import java.time.Instant;

public class AddEventRequest {
    private String nameEvent;
    private String description;
    private String location;
    private Instant dateTime;

    public AddEventRequest() {
    }

    public AddEventRequest(String nameEvent, String description, String location, Instant dateTime) {
        this.nameEvent = nameEvent;
        this.description = description;
        this.location = location;
        this.dateTime = dateTime;
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

    public Instant getDateTime() {
        return dateTime;
    }

    public void setDateTime(Instant dateTime) {
        this.dateTime = dateTime;
    }
}
