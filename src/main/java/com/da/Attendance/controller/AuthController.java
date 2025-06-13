package com.da.Attendance.controller;

import com.da.Attendance.dto.request.User.GoogleLoginRequest;
import com.da.Attendance.dto.request.User.UserLoginRequest;
import com.da.Attendance.dto.request.User.UserRegisterRequest;
import com.da.Attendance.dto.response.ApiResponse;
import com.da.Attendance.dto.response.User.UserLoginResponse;
import com.da.Attendance.dto.response.User.UserRegisterResponse;
import com.da.Attendance.model.User;
import com.da.Attendance.service.OtpService;
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
    @Autowired
    private OtpService otpService;
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
    @PostMapping("/login/google")
    public ResponseEntity<ApiResponse> loginWithGoogle(@RequestBody GoogleLoginRequest request) {
        try {
            UserLoginResponse response = userService.loginWithGoogle(request.getIdToken());
            return ResponseEntity.ok(new ApiResponse("Login with Google success", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse("Login with Google failed: " + e.getMessage(), null));
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
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        try {
            User user = userService.getUserByEmail(email);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse("Email not found", null));
            }
            otpService.generateAndSendOtp(email, user.getId());
            return ResponseEntity.ok(new ApiResponse("Send otp success", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Send otp failed: " + e.getMessage(), null));
        }
    }
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestParam String userId, @RequestParam String otp) {
        try {
            boolean valid = otpService.verifyOtp(userId, otp);
            if (!valid) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse("OTP is invalid or expired", null));
            }
            return ResponseEntity.ok(new ApiResponse("OTP is valid, you can change password", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("OTP authentication error: " + e.getMessage(), null));
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
    @PostMapping("/change-password")
    public ResponseEntity<?> changePasswordByOtp(@RequestParam String email, @RequestParam String password) {
        try {
            userService.changePasswordByOtp(email, password);
            return ResponseEntity.ok(new ApiResponse("Change password success", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Change password error: " + e.getMessage(), null));
        }
    }
}
