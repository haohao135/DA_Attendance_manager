package com.da.Attendance.controller;

import com.da.Attendance.dto.request.User.UserLoginRequest;
import com.da.Attendance.dto.request.User.UserRegisterRequest;
import com.da.Attendance.dto.response.ApiResponse;
import com.da.Attendance.dto.response.User.UserLoginResponse;
import com.da.Attendance.dto.response.User.UserRegisterResponse;
import com.da.Attendance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {
    @Autowired
    private UserService userService;
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody UserRegisterRequest userRegisterRequest){
        try {
            UserRegisterResponse userRegisterResponse = userService.register(userRegisterRequest);
            return ResponseEntity.ok(new ApiResponse("Register success", userRegisterResponse));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Register failed " + e.getMessage(), null));
        }
    }
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody UserLoginRequest userLoginRequest){
        try {
            UserLoginResponse userLoginResponse = userService.login(userLoginRequest);
            return ResponseEntity.ok(new ApiResponse("Login success", userLoginResponse));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Login failed " + e.getMessage(), null));
        }
    }

    @GetMapping("/me")
    public String getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        } else {
            return "No user logged in";
        }
    }
}
