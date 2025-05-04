package com.da.Attendance.controller;

import com.da.Attendance.dto.request.User.UserUpdateAdminRequest;
import com.da.Attendance.dto.response.ApiResponse;
import com.da.Attendance.model.User;
import com.da.Attendance.model.enums.UserRole;
import com.da.Attendance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@CrossOrigin("*")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/full-name/{id}")
    public ResponseEntity<ApiResponse> updateUserName(@PathVariable String id, @RequestBody String fullName){
        try {
            userService.updateUserFullName(id, fullName);
            return ResponseEntity.ok(new ApiResponse("Update username success", null));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Update username failed " + e.getMessage(), null));
        }
    }
    @PostMapping("/phone-number/{id}")
    public ResponseEntity<ApiResponse> updatePhoneNumber(@PathVariable String id, @RequestBody String phoneNumber){
        try {
            userService.updateUserPhoneNumber(id, phoneNumber);
            return ResponseEntity.ok(new ApiResponse("Update phone number success", null));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Update phone number failed " + e.getMessage(), null));
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
}
