package com.da.Attendance.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Document
public class Event {
    @Id
    private String id;
    private String nameEvent;
    private String location;
    private String description;
    private Instant dateTime;
    private String organizerId;
    private List<String> participantIds;
}
