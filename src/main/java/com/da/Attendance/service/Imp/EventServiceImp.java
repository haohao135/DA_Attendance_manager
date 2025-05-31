package com.da.Attendance.service.Imp;

import com.da.Attendance.dto.request.Event.AddEventRequest;
import com.da.Attendance.dto.request.Event.AddStudentsRequest;
import com.da.Attendance.dto.request.Event.UpdateEventRequest;
import com.da.Attendance.model.Event;
import com.da.Attendance.model.EventRecord;
import com.da.Attendance.model.User;
import com.da.Attendance.model.enums.AttendanceStatus;
import com.da.Attendance.repository.EventRecordRepository;
import com.da.Attendance.repository.EventRepository;
import com.da.Attendance.repository.UserRepository;
import com.da.Attendance.service.EventRecordService;
import com.da.Attendance.service.EventService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.impl.xb.xsdschema.Attribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class EventServiceImp implements EventService {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EventRecordService eventRecordService;
    @Autowired
    private EventRecordRepository eventRecordRepository;
    @Autowired
    private UserRepository userRepository;

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
        Event event = new Event();
        event.setNameEvent(addEventRequest.getNameEvent());
        event.setLocation(addEventRequest.getLocation());
        event.setTimeStart(addEventRequest.getTimeStart());
        event.setTimeEnd(addEventRequest.getTimeEnd());
        event.setOrganizerId(addEventRequest.getOrganizerId());
        Event saveEvent = eventRepository.save(event);
        eventRecordService.add(saveEvent);
        return saveEvent;
    }

    @Override
    public Event addStudent(String id, String studentId) {
        Event event = findEventById(id);
        if (!event.getParticipantIds().contains(studentId)) {
            event.getParticipantIds().add(studentId);
            Event saveEvent = eventRepository.save(event);
            eventRecordService.addOne(saveEvent, studentId);
        }
        return event;
    }

    @Override
    public Event addStudentList(AddStudentsRequest addStudentsRequest) {
        Event event = findEventById(addStudentsRequest.getId());
        for (String studentId : addStudentsRequest.getStudentIds()) {
            if (!event.getParticipantIds().contains(studentId)) {
                event.getParticipantIds().add(studentId);
            }
        }
        return eventRepository.save(event);
    }

    @Override
    public Event updateEvent(String id, UpdateEventRequest updateEventRequest) {
        Event event = findEventById(id);
        event.setNameEvent(updateEventRequest.getNameEvent());
        event.setTimeStart(updateEventRequest.getTimeStart());
        event.setTimeEnd(updateEventRequest.getTimeEnd());
        event.setOrganizerId(updateEventRequest.getOrganizerId());
        event.setLocation(updateEventRequest.getLocation());
        return eventRepository.save(event);
    }

    @Override
    public Event updateTime(String id, Instant instant) {
//        Event event = findEventById(id);
//
        return null;
    }

    @Override
    public List<Event> getEventByStudentId(String id) {
        return eventRepository.findByParticipantIdsContaining(id);
    }

    @Override
    public void deleteEvent(String id) {
        eventRepository.deleteById(id);
    }

    @Override
    public List<Event> getAllEventsByOrganizerId(String organizerId) {
        return eventRepository.findByOrganizerId(organizerId);
    }

    @Override
    public List<Event> getUpcomingEventsByOrganizerId(String organizerId) {
        Instant now = Instant.now();
        return eventRepository.findByOrganizerIdAndTimeStartAfter(organizerId, now, Sort.by(Sort.Direction.ASC, "timeStart"));
    }

    @Override
    public List<Event> getPastEventsByOrganizerId(String organizerId) {
        Instant now = Instant.now();
        return eventRepository.findByOrganizerIdAndTimeEndBefore(organizerId, now, Sort.by(Sort.Direction.ASC, "timeStart"));
    }

    @Override
    public List<Event> getAllEvent() {
        return eventRepository.findAll();
    }

    @Override
    public ByteArrayInputStream exportEventAttendanceReport(String eventId) throws IOException {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("event not found"));

        List<String> studentIds = event.getParticipantIds();

        // Lấy danh sách điểm danh
        List<EventRecord> records = eventRecordRepository.findByEventId(eventId);

        // Map studentId -> attendanceStatus
        Map<String, AttendanceStatus> attendanceMap = records.stream()
                .collect(Collectors.toMap(EventRecord::getStudentId, EventRecord::getAttendanceStatus));

        boolean isEventStarted = Instant.now().isAfter(event.getTimeStart());

        // Bắt đầu tạo Excel
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Attendance");

        // Header
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Họ tên");
        header.createCell(1).setCellValue("Trạng thái");

        for (int i = 0; i < studentIds.size(); i++) {
            String studentId = studentIds.get(i);
            Row row = sheet.createRow(i + 1);
            User user = userRepository.findById(studentId).orElseThrow(()-> new RuntimeException("user not found"));
            row.createCell(0).setCellValue(user.getFullName());

            String statusStr = "";
            if (isEventStarted) {
                AttendanceStatus status = attendanceMap.get(studentId);
                if (status != null) {
                    if (status == AttendanceStatus.PRESENT) {
                        statusStr = "Có mặt";
                    } else if (status == AttendanceStatus.ABSENT) {
                        statusStr = "Vắng";
                    }
                }
            }

            row.createCell(1).setCellValue(statusStr);
        }

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return new ByteArrayInputStream(out.toByteArray());
    }
}
