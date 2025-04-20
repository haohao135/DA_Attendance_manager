package com.da.Attendance.dto.request.Event;

import java.time.Instant;

public class UpdateEventRequest {
    private String nameEvent;
    private String description;
    private String location;

    public UpdateEventRequest() {
    }

    public UpdateEventRequest(String nameEvent, String description, String location) {
        this.nameEvent = nameEvent;
        this.description = description;
        this.location = location;
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
}
