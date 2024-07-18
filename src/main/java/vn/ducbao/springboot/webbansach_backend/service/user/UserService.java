package vn.ducbao.springboot.webbansach_backend.service.user;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import vn.ducbao.springboot.webbansach_backend.dto.request.JwtRequest;
import vn.ducbao.springboot.webbansach_backend.entity.User;

public interface UserService {
    public ResponseEntity<?> register(User user);
    public ResponseEntity<?> changePassword(JsonNode jsonNode);
    public User findByUserName(String userName);
    public ResponseEntity<?> activeUser(String email, String activeCode);
    public  ResponseEntity<?> forgotPassword(JsonNode jsonNode);
    public  ResponseEntity<?> updateProfile(JsonNode jsonNode);
    public ResponseEntity<?> changeAvatar(JsonNode jsonNode);
    public  ResponseEntity<?> save(JsonNode jsonNode, String option);
}
