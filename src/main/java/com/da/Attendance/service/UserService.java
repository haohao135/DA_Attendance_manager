package com.da.Attendance.service;

import com.da.Attendance.dto.request.User.ChangePasswordRequest;
import com.da.Attendance.dto.request.User.UserLoginRequest;
import com.da.Attendance.dto.request.User.UserRegisterRequest;
import com.da.Attendance.dto.response.User.UserLoginResponse;
import com.da.Attendance.dto.response.User.UserRegisterResponse;
import com.da.Attendance.model.User;
import com.da.Attendance.model.enums.UserRole;

import java.util.List;

public interface UserService {
    UserRegisterResponse register(UserRegisterRequest userRegisterRequest);
    UserLoginResponse login(UserLoginRequest userLoginRequest);
    void updateUserFullName(String id, String name);
    void updateUserPhoneNumber(String id, String phoneNumber);
    void updateAvatar(String id, String avatarId);
    void changePassword(String id, ChangePasswordRequest changePasswordRequest);
    User getUserById(String id);
    List<User> getAllUser();
    List<User> getUserByRole(UserRole userRole);
    void changeRole(String id, UserRole userRole);
    void deleteRole(String id, UserRole userRole);
    void deleteUserById(String id);
}
