package com.da.Attendance.model;

import com.da.Attendance.model.enums.UserRole;
import com.da.Attendance.model.enums.UserStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Document
public class User {
    @Id
    private String id;
    private String password;
    private String fullName;
    private String numberPhone;
    private String email;
    private String avatarId;
    private UserStatus userStatus;
    private List<UserRole> userRole = new ArrayList<>();
    private Instant createAt = Instant.now();
    public User() {}

    public User(String password, String fullName, String email) {
        this.password = password;
        this.fullName = fullName;
        this.email = email;
    }

    public User(String id, String password, String fullName, String numberPhone, String email, String avatarId, List<UserRole> userRole, Instant createAt) {
        this.id = id;
        this.password = password;
        this.fullName = fullName;
        this.numberPhone = numberPhone;
        this.email = email;
        this.avatarId = avatarId;
        this.userRole = userRole;
        this.createAt = createAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(String avatarId) {
        this.avatarId = avatarId;
    }

    public List<UserRole> getUserRole() {
        return userRole;
    }

    public void setUserRole(List<UserRole> userRole) {
        this.userRole = userRole;
    }

    public Instant getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Instant createAt) {
        this.createAt = createAt;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }
}
