package com.da.Attendance.controller;

import com.da.Attendance.dto.request.User.ChangePasswordRequest;
import com.da.Attendance.dto.request.User.FilterUserRequest;
import com.da.Attendance.dto.request.User.UserUpdateAdminRequest;
import com.da.Attendance.dto.response.ApiResponse;
import com.da.Attendance.dto.response.User.UserAttendanceRecordResponse;
import com.da.Attendance.model.User;
import com.da.Attendance.model.enums.UserRole;
import com.da.Attendance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@CrossOrigin("*")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/full-name")
    public ResponseEntity<ApiResponse> updateUserName(@RequestParam String email, @RequestParam String fullName){
        try {
            userService.updateUserFullName(email, fullName);
            return ResponseEntity.ok(new ApiResponse("Update username success", null));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Update username failed " + e.getMessage(), null));
        }
    }
    @PostMapping("/phone-number")
    public ResponseEntity<ApiResponse> updatePhoneNumber(@RequestParam String email, @RequestParam String phoneNumber){
        try {
            userService.updateUserPhoneNumber(email, phoneNumber);
            return ResponseEntity.ok(new ApiResponse("Update phone number success", null));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Update phone number failed " + e.getMessage(), null));
        }
    }
    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse> updatePassword(@RequestBody ChangePasswordRequest request){
        try {
            userService.changePassword(request);
            return ResponseEntity.ok(new ApiResponse("Password changed successfully", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse("Update password failed: " + e.getMessage(), null));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Internal server error: " + e.getMessage(), null));
        }
    }

    @GetMapping("/by-email")
    public ResponseEntity<ApiResponse> getUserByEmail(@RequestParam String email){
        try {
            User user = userService.getUserByEmail(email);
            return ResponseEntity.ok(new ApiResponse("Get user success", user));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Get user failed " + e.getMessage(), null));
        }
    }
    @GetMapping("/by-id")
    public ResponseEntity<ApiResponse> getUserById(@RequestParam String id){
        try {
            User user = userService.getUserById(id);
            return ResponseEntity.ok(new ApiResponse("Get user success", user));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Get user failed " + e.getMessage(), null));
        }
    }
    @GetMapping("/get/all")
    public ResponseEntity<ApiResponse> getAllUser(){
        try {
            List<User> user = userService.getAllUser();
            return ResponseEntity.ok(new ApiResponse("Get all user success", user));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Get all user failed " + e.getMessage(), null));
        }
    }
    @PostMapping("/update")
    public ResponseEntity<ApiResponse> updateUserByAdmin(@RequestParam String id,
                                                         @RequestBody UserUpdateAdminRequest userUpdateAdminRequest){
        try {
            userService.updateUserByAdmin(id, userUpdateAdminRequest);
            return ResponseEntity.ok(new ApiResponse("Update user success", null));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Update user failed " + e.getMessage(), null));
        }
    }
    @PostMapping("/update/role")
    public ResponseEntity<ApiResponse> updateUserRoleByAdmin(@RequestParam String id,
                                                             @RequestBody UserRole userRole){
        try {
            userService.changeRole(id, userRole);
            return ResponseEntity.ok(new ApiResponse("Update user role success", null));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Update user role failed " + e.getMessage(), null));
        }
    }
    @PostMapping("/delete")
    public ResponseEntity<ApiResponse> deleteUserById(@RequestParam String id){
        try {
            userService.deleteUserById(id);
            return ResponseEntity.ok(new ApiResponse("Delete user success", null));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Delete user failed " + e.getMessage(), null));
        }
    }
    @GetMapping("/get/no-attendance")
    public ResponseEntity<ApiResponse> getUserNoAttendance(@RequestParam String sessionId){
        try {
            List<UserAttendanceRecordResponse> user = userService.getUsersNoAttendance(sessionId);
            return ResponseEntity.ok(new ApiResponse("Get all user no attendance success", user));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Get all user no attendance failed " + e.getMessage(), null));
        }
    }
    @GetMapping("/get/took-attendance")
    public ResponseEntity<ApiResponse> getUserTookAttendance(@RequestParam String sessionId){
        try {
            List<UserAttendanceRecordResponse> user = userService.getUsersTookAttendance(sessionId);
            return ResponseEntity.ok(new ApiResponse("Get all user took attendance success", user));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Get all user took attendance failed " + e.getMessage(), null));
        }
    }
    @GetMapping("/get/no-attendance/event")
    public ResponseEntity<ApiResponse> getUserNoAttendanceEvent(@RequestParam String eventId){
        try {
            List<UserAttendanceRecordResponse> user = userService.getUsersNoAttendanceEvent(eventId);
            return ResponseEntity.ok(new ApiResponse("Get all user no attendance event success", user));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Get all user no attendance event failed " + e.getMessage(), null));
        }
    }
    @GetMapping("/get/took-attendance/event")
    public ResponseEntity<ApiResponse> getUserTookAttendanceEvent(@RequestParam String eventId){
        try {
            List<UserAttendanceRecordResponse> user = userService.getUsersTookAttendanceEvent(eventId);
            return ResponseEntity.ok(new ApiResponse("Get all user took attendance event success", user));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Get all user took attendance event failed " + e.getMessage(), null));
        }
    }
    @PostMapping("/get/filter-by-role")
    public ResponseEntity<ApiResponse> getUsersByRole(@RequestBody FilterUserRequest filterUserRequest) {
        try {
            List<User> users = userService.getUsersByRoleExcludingIds(filterUserRequest.getUserRole(), filterUserRequest.getStudentId());
            return ResponseEntity.ok(new ApiResponse("Get users success", users));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Get users failed " + e.getMessage(), null));
        }
    }
    @PostMapping("/add/avatar")
    public ResponseEntity<ApiResponse> addAvatar(@RequestParam String id, @RequestParam MultipartFile multipartFile) {
        try {
            userService.addAvatar(id, multipartFile);
            return ResponseEntity.ok(new ApiResponse("Add avatar success", null));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("Add avatar failed " + e.getMessage(), null));
        }
    }
}
