package com.da.Attendance.repository;

import com.da.Attendance.model.Image;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends MongoRepository<Image, String> {
    Image findByFileName(String name);
}
