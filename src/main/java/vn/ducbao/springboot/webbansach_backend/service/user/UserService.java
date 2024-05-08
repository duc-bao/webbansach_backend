package vn.ducbao.springboot.webbansach_backend.service.user;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import vn.ducbao.springboot.webbansach_backend.entity.User;

public interface UserService extends UserDetailsService {
    public ResponseEntity<?> register(User user);
    public User findByUserName(String userName);
}
