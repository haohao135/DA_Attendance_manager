package com.da.Attendance.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class Classroom {
    @Id
    private String id;
    private String className;
    private String subject;
    private String teacherId;
    private List<String> studentIds;
}
