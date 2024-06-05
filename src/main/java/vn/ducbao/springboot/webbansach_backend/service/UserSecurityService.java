package vn.ducbao.springboot.webbansach_backend.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import vn.ducbao.springboot.webbansach_backend.entity.User;

public interface UserSecurityService extends UserDetailsService {
    public User findByUsername(String username);
}
