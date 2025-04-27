package com.da.Attendance.controller;

import com.da.Attendance.dto.response.ApiResponse;
import com.da.Attendance.model.User;
import com.da.Attendance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
