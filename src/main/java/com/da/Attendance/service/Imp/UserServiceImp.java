package com.da.Attendance.service.Imp;

import com.da.Attendance.dto.request.User.ChangePasswordRequest;
import com.da.Attendance.dto.request.User.UserLoginRequest;
import com.da.Attendance.dto.request.User.UserRegisterRequest;
import com.da.Attendance.dto.request.User.UserUpdateAdminRequest;
import com.da.Attendance.dto.response.User.UserAttendanceRecordResponse;
import com.da.Attendance.dto.response.User.UserLoginResponse;
import com.da.Attendance.dto.response.User.UserRegisterResponse;
import com.da.Attendance.model.AttendanceRecord;
import com.da.Attendance.model.EventRecord;
import com.da.Attendance.model.Image;
import com.da.Attendance.model.User;
import com.da.Attendance.model.enums.AttendanceMethod;
import com.da.Attendance.model.enums.AttendanceStatus;
import com.da.Attendance.model.enums.UserRole;
import com.da.Attendance.model.enums.UserStatus;
import com.da.Attendance.repository.AttendanceRecordRepository;
import com.da.Attendance.repository.EventRecordRepository;
import com.da.Attendance.repository.UserRepository;
import com.da.Attendance.security.JwtUtil;
import com.da.Attendance.service.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

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
    @Autowired
    private AttendanceRecordRepository attendanceRecordRepository;
    @Autowired
    private EventRecordRepository eventRecordRepository;
    @Autowired
    private ImageServiceImp imageServiceImp;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;


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
        user.setUserStatus(UserStatus.ACTIVE);
        if (user.getUserRole() == null) {
            user.setUserRole(new ArrayList<>());
        }
        user.getUserRole().add(UserRole.USER);
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
        if(user.getUserStatus() == UserStatus.LOCK){
            throw new RuntimeException("account is locked");
        }
        return new UserLoginResponse(
                user.getFullName(), jwtUtil.generateToken(user.getEmail(), user.getUserRole())
        );
    }

    @Override
    public void updateUserFullName(String email, String name) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("user not found"));
        user.setFullName(name);
        userRepository.save(user);
    }

    @Override
    public void updateUserPhoneNumber(String email, String phoneNumber) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("user not found"));
        user.setNumberPhone(phoneNumber);
        userRepository.save(user);
    }

    @Override
    public void updateAvatar(String email, String avatarId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("user not found"));
        user.setAvatarId(avatarId);
        userRepository.save(user);
    }

    public void changePassword(ChangePasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("user not found"));
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("old password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public void changePasswordByOtp(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("user not found"));
        user.setPassword(passwordEncoder.encode(password));
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
    public User getUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isEmpty()){
            throw new RuntimeException("user not found");
        }
        return user.get();
    }

    @Override
    public void changeRole(String id, UserRole userRole) {
        User user = getUserById(id);
        user.getUserRole().clear();
        user.getUserRole().add(userRole);
        userRepository.save(user);
    }

    @Override
    public void deleteRole(String id, UserRole userRole) {
        User user = getUserById(id);
        user.getUserRole().remove(userRole);
        userRepository.save(user);
    }

    @Override
    public void deleteUserById(String id) {
        userRepository.deleteById(id);
    }

    @Override
    public void updateUserByAdmin(String id, UserUpdateAdminRequest userUpdateAdminRequest) {
        User user = getUserById(id);
        user.setEmail(userUpdateAdminRequest.getEmail());
        user.setFullName(userUpdateAdminRequest.getFullName());
        user.setNumberPhone(userUpdateAdminRequest.getNumberPhone());
        userRepository.save(user);
    }

    @Override
    public List<UserAttendanceRecordResponse> getUsersNoAttendance(String sessionId) {
        List<AttendanceRecord> attendanceRecords =
                attendanceRecordRepository.findByAttendanceSessionIdAndStatus(sessionId, AttendanceStatus.ABSENT);

        List<UserAttendanceRecordResponse> result = new ArrayList<>();

        for (AttendanceRecord record : attendanceRecords) {
            try {
                User user = getUserById(record.getStudentId());
                result.add(new UserAttendanceRecordResponse(record.getId(), user, record.getStatus()));
            } catch (RuntimeException e) {
                System.err.println("User not found for studentId: " + record.getStudentId());
            }
        }
        return result;
    }


    @Override
    public List<UserAttendanceRecordResponse> getUsersTookAttendance(String sessionId) {
        List<AttendanceRecord> attendanceRecords =
                attendanceRecordRepository.findByAttendanceSessionIdAndStatusIn(
                        sessionId, List.of(AttendanceStatus.PRESENT, AttendanceStatus.LATE));
        List<UserAttendanceRecordResponse> result = new ArrayList<>();

        for (AttendanceRecord record : attendanceRecords) {
            try {
                User user = getUserById(record.getStudentId());
                result.add(new UserAttendanceRecordResponse(record.getId(), user, record.getStatus()));
            } catch (RuntimeException e) {
                System.err.println("User not found for studentId: " + record.getStudentId());
            }
        }
        return result;
    }

    @Override
    public List<UserAttendanceRecordResponse> getUsersNoAttendanceEvent(String eventId) {
        List<EventRecord> eventRecords = eventRecordRepository.
                findByEventIdAndAttendanceStatus(eventId, AttendanceStatus.ABSENT);
        List<UserAttendanceRecordResponse> result = new ArrayList<>();
        for (EventRecord record : eventRecords) {
            try {
                User user = getUserById(record.getStudentId());
                result.add(new UserAttendanceRecordResponse(record.getId(), user, record.getAttendanceStatus()));
            } catch (RuntimeException e) {
                System.err.println("user not found for studentId: " + record.getStudentId());
            }
        }
        return result;
    }

    @Override
    public List<UserAttendanceRecordResponse> getUsersTookAttendanceEvent(String eventId) {
        List<EventRecord> eventRecords = eventRecordRepository.
                findByEventIdAndAttendanceStatus(eventId, AttendanceStatus.PRESENT);
        List<UserAttendanceRecordResponse> result = new ArrayList<>();
        for (EventRecord record : eventRecords) {
            try {
                User user = getUserById(record.getStudentId());
                result.add(new UserAttendanceRecordResponse(record.getId(), user, record.getAttendanceStatus()));
            } catch (RuntimeException e) {
                System.err.println("user not found for studentId: " + record.getStudentId());
            }
        }
        return result;
    }

    @Override
    public List<User> getUsersByRoleExcludingIds(UserRole role, List<String> excludeIds) {
        return userRepository.findByUserRoleInAndIdNotIn(Collections.singletonList(role), excludeIds);
    }

    @Override
    public void addAvatar(String id, MultipartFile multipartFile) {
        User user = getUserById(id);
        Image image = imageServiceImp.addImage(multipartFile);
        user.setAvatarId(image.getFileName());
        userRepository.save(user);
        Map<String, String> payload = Map.of(
                "userId", user.getId(),
                "avatarUrl", user.getAvatarId()
        );
        messagingTemplate.convertAndSend("/topic/avatar-update", payload);
    }

    @Override
    public UserLoginResponse loginWithGoogle(String idTokenString) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier
                    .Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList("166233101707-lnku861um234bf76aa1p771mpn86ilrc.apps.googleusercontent.com"))
                    .build();


            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken == null) {
                throw new RuntimeException("Invalid ID token");
            }

            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");

            User user = userRepository.findByEmail(email).orElse(null);

            if (user == null) {
                user = new User();
                user.setEmail(email);
                user.setFullName(name);
                user.setUserRole(Collections.singletonList(UserRole.USER));
                user.setUserStatus(UserStatus.ACTIVE);

                // tải ảnh avatar từ url, convert thành MultipartFile
                MultipartFile avatarFile = downloadImageAsMultipartFile(pictureUrl);
                Image image = imageServiceImp.addImage(avatarFile);
                user.setAvatarId(image.getFileName());

                user = userRepository.save(user);
            }

            if (user.getUserStatus() == UserStatus.LOCK) {
                throw new RuntimeException("account is locked");
            }

            String token = jwtUtil.generateToken(user.getEmail(), user.getUserRole());
            return new UserLoginResponse(user.getFullName(), token);
        } catch (Exception e) {
            throw new RuntimeException("Google login failed: " + e.getMessage());
        }
    }
    private MultipartFile downloadImageAsMultipartFile(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        String fileName = UUID.randomUUID() + ".jpg";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (InputStream is = url.openStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
        }
        return new MockMultipartFile(fileName, fileName, "image/jpeg", baos.toByteArray());
    }

}
