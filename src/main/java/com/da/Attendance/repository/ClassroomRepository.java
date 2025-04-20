package com.da.Attendance.repository;

import com.da.Attendance.model.Classroom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassroomRepository extends MongoRepository<Classroom, String> {
    List<Classroom> findByStudentIdsContaining(String studentId);

}
