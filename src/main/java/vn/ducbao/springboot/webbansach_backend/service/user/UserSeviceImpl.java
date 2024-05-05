package vn.ducbao.springboot.webbansach_backend.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.ducbao.springboot.webbansach_backend.dao.UserRepository;
import vn.ducbao.springboot.webbansach_backend.entity.Notification;
import vn.ducbao.springboot.webbansach_backend.entity.User;

@Service
public class UserSeviceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public ResponseEntity<?> register(User user) {
        if (userRepository.existsByUserName(user.getUserName())) {
            return  ResponseEntity.badRequest().body(new Notification("Username đã tồn tại"));
        }
        if (userRepository.existsByEmail((user.getEmail()))) {
            return  ResponseEntity.badRequest().body(new Notification("Email đã tồn tại"));
        }
        userRepository.save(user);
        return  ResponseEntity.ok("Tạo thành công");
    }
}
