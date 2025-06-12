package com.da.Attendance.repository;

import com.da.Attendance.model.Otp;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface OtpRepository extends MongoRepository<Otp, String> {
    Optional<Otp> findTopByUserIdOrderByExpirationDesc(String userId);
}
