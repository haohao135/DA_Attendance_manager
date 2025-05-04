package com.da.Attendance.dto.request.Event;

import java.time.Instant;

public class UpdateEventRequest {
    private String nameEvent;
    private String location;
    private Instant timeStart;
    private Instant timeEnd;
    private String organizerId;

    public UpdateEventRequest() {
    }

    public UpdateEventRequest(String nameEvent, String location, Instant timeStart, Instant timeEnd, String organizerId) {
        this.nameEvent = nameEvent;
        this.location = location;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.organizerId = organizerId;
    }

    public String getNameEvent() {
        return nameEvent;
    }

    public void setNameEvent(String nameEvent) {
        this.nameEvent = nameEvent;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public String getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(String organizerId) {
        this.organizerId = organizerId;
    }
}
