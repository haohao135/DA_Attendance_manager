package com.da.Attendance.service.Imp;

import com.da.Attendance.dto.request.User.ChangePasswordRequest;
import com.da.Attendance.dto.request.User.UserLoginRequest;
import com.da.Attendance.dto.request.User.UserRegisterRequest;
import com.da.Attendance.dto.response.User.UserLoginResponse;
import com.da.Attendance.dto.response.User.UserRegisterResponse;
import com.da.Attendance.model.User;
import com.da.Attendance.model.enums.UserRole;
import com.da.Attendance.repository.UserRepository;
import com.da.Attendance.security.JwtUtil;
import com.da.Attendance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public UserRegisterResponse register(UserRegisterRequest userRegisterRequest) {
        if (userRepository.findByEmail(userRegisterRequest.getEmail()).isPresent()) {
            throw new RuntimeException("email already exists");
        }
        User user = new User(
                passwordEncoder.encode(userRegisterRequest.getPassword()),
                userRegisterRequest.getFullName(),
                userRegisterRequest.getEmail()
        );
        user.setUserRole(UserRole.USER);
        userRepository.save(user);
        return new UserRegisterResponse(user.getEmail(), user.getFullName());
    }

    @Override
    public UserLoginResponse login(UserLoginRequest userLoginRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    userLoginRequest.getEmail(), userLoginRequest.getPassword()));
        } catch (Exception e){
            throw new RuntimeException("invalid email or password");
        }
        User user = userRepository.findByEmail(userLoginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("user not found"));
        return new UserLoginResponse(
                user.getFullName(), jwtUtil.generateToken(user.getEmail(), user.getUserRole().toString())
        );
    }

    @Override
    public void updateUserFullName(String id, String name) {
        User user = getUserById(id);
        user.setFullName(name);
        userRepository.save(user);
    }

    @Override
    public void updateUserPhoneNumber(String id, String phoneNumber) {
        User user = getUserById(id);
        user.setNumberPhone(phoneNumber);
        userRepository.save(user);
    }

    @Override
    public void updateAvatar(String id, String avatarId) {
        User user = getUserById(id);
        user.setAvatarId(avatarId);
        userRepository.save(user);
    }

    @Override
    public void changePassword(String id, ChangePasswordRequest changePasswordRequest) {
        User user = getUserById(id);
        if(!passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())){
            throw new RuntimeException("password is incorrect");
        } else if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmPassword())) {
            throw new RuntimeException("new password not match");
        }
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public User getUserById(String id) {
        Optional<User> users = userRepository.findById(id);
        if(users.isEmpty()){
            throw new RuntimeException("user not found");
        }
        return users.get();
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public List<User> getUserByRole(UserRole userRole) {
        return userRepository.findByUserRole(userRole);
    }

    @Override
    public void changeRole(String id, UserRole userRole) {
        User user = getUserById(id);
        user.setUserRole(userRole);
        userRepository.save(user);
    }

    @Override
    public void deleteUserById(String id) {
        userRepository.deleteById(id);
    }
}
