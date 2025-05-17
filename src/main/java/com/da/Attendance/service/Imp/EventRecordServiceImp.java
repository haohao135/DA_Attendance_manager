package com.da.Attendance.service.Imp;

import com.da.Attendance.model.Event;
import com.da.Attendance.model.EventRecord;
import com.da.Attendance.model.enums.AttendanceMethod;
import com.da.Attendance.model.enums.AttendanceStatus;
import com.da.Attendance.repository.EventRecordRepository;
import com.da.Attendance.service.EventRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventRecordServiceImp implements EventRecordService {
    @Autowired
    private EventRecordRepository eventRecordRepository;
    @Override
    public boolean add(Event event) {
        List<EventRecord> eventRecords = event.getParticipantIds().stream().map(studentId -> {
            EventRecord eventRecord = new EventRecord();
            eventRecord.setStudentId(studentId);
            eventRecord.setEventId(event.getId());
            eventRecord.setMethod(AttendanceMethod.QR_CODE);
            eventRecord.setAttendanceStatus(AttendanceStatus.ABSENT);
            return eventRecord;
        }).collect(Collectors.toList());
        eventRecordRepository.saveAll(eventRecords);
        return true;
    }

    @Override
    public boolean addOne(Event event, String studentId) {
        EventRecord eventRecord = new EventRecord();
        eventRecord.setStudentId(studentId);
        eventRecord.setEventId(event.getId());
        eventRecord.setMethod(AttendanceMethod.QR_CODE);
        eventRecord.setAttendanceStatus(AttendanceStatus.ABSENT);
        eventRecordRepository.save(eventRecord);
        return true;
    }

    @Override
    public EventRecord updateStatus(String id, AttendanceStatus attendanceStatus) {
        Optional<EventRecord> eventRecord = eventRecordRepository.findById(id);
        if(eventRecord.isEmpty()){
            throw new RuntimeException("event record not found");
        }
        EventRecord newEvent = eventRecord.get();
        newEvent.setAttendanceStatus(attendanceStatus);
        return eventRecordRepository.save(newEvent);
    }
}
