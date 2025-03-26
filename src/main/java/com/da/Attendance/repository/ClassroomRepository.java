package com.da.Attendance.repository;

import com.da.Attendance.model.Classroom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassroomRepository extends MongoRepository<Classroom, String> {
}
