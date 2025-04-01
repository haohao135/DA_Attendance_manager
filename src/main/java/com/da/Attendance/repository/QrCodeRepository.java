package com.da.Attendance.repository;

import com.da.Attendance.model.QrCode;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface QrCodeRepository extends MongoRepository<QrCode, String> {
    Optional<QrCode> findBySessionId(String id);
    void deleteBySessionId(String sessionId);
    void deleteByExpiresAtBefore(Instant now);
}
