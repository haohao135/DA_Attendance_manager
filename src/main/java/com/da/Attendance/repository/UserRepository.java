package com.da.Attendance.repository;

import com.da.Attendance.model.User;
import com.da.Attendance.model.enums.UserRole;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    List<User> findByUserRole(UserRole userRole);
}
