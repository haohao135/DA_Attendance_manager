package com.da.Attendance.model;

import com.da.Attendance.model.enums.ReportType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document
public class Report {
    @Id
    private String id;
    private String generatedBy;
    private ReportType type;
    private String data;
    private Instant createAt = Instant.now();
}
