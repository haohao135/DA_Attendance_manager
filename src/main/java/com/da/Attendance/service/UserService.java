package com.da.Attendance.service;

import com.da.Attendance.dto.request.User.ChangePasswordRequest;
import com.da.Attendance.dto.request.User.UserLoginRequest;
import com.da.Attendance.dto.request.User.UserRegisterRequest;
import com.da.Attendance.dto.request.User.UserUpdateAdminRequest;
import com.da.Attendance.dto.response.User.UserAttendanceRecordResponse;
import com.da.Attendance.dto.response.User.UserLoginResponse;
import com.da.Attendance.dto.response.User.UserRegisterResponse;
import com.da.Attendance.model.User;
import com.da.Attendance.model.enums.UserRole;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    UserRegisterResponse register(UserRegisterRequest userRegisterRequest);
    UserLoginResponse login(UserLoginRequest userLoginRequest);
    void updateUserFullName(String email, String name);
    void updateUserPhoneNumber(String email, String phoneNumber);
    void updateAvatar(String email, String avatarId);
    void changePassword(ChangePasswordRequest changePasswordRequest);
    User getUserById(String id);
    List<User> getAllUser();
    List<User> getUserByRole(UserRole userRole);
    User getUserByEmail(String email);
    void changeRole(String id, UserRole userRole);
    void deleteRole(String id, UserRole userRole);
    void deleteUserById(String id);
    void updateUserByAdmin(String id, UserUpdateAdminRequest userUpdateAdminRequest);
    List<UserAttendanceRecordResponse> getUsersNoAttendance(String sessionId);
    List<UserAttendanceRecordResponse> getUsersTookAttendance(String sessionId);
    List<UserAttendanceRecordResponse> getUsersNoAttendanceEvent(String eventId);
    List<UserAttendanceRecordResponse> getUsersTookAttendanceEvent(String eventId);
    List<User> getUsersByRoleExcludingIds(UserRole role, List<String> excludeIds);
    void addAvatar(String id, MultipartFile multipartFile);
    UserLoginResponse loginWithGoogle(String idToken);
}
