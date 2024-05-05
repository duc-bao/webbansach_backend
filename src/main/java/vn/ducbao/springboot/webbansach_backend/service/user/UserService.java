package vn.ducbao.springboot.webbansach_backend.service.user;

import org.springframework.http.ResponseEntity;
import vn.ducbao.springboot.webbansach_backend.entity.User;

public interface UserService {
    public ResponseEntity<?> register(User user);
}
