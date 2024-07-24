package vn.ducbao.springboot.webbansach_backend.service.user;

import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.JsonNode;

import vn.ducbao.springboot.webbansach_backend.entity.User;

public interface UserService {
    public ResponseEntity<?> register(User user);

    public ResponseEntity<?> changePassword(JsonNode jsonNode);

    public User findByUserName(String userName);

    public ResponseEntity<?> activeUser(String email, String activeCode);

    public ResponseEntity<?> forgotPassword(JsonNode jsonNode);

    public ResponseEntity<?> updateProfile(JsonNode jsonNode);

    public ResponseEntity<?> changeAvatar(JsonNode jsonNode);

    public ResponseEntity<?> save(JsonNode jsonNode, String option);
}
