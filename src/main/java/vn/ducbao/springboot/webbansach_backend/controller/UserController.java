package vn.ducbao.springboot.webbansach_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.ducbao.springboot.webbansach_backend.entity.User;
import vn.ducbao.springboot.webbansach_backend.service.user.UserService;
import vn.ducbao.springboot.webbansach_backend.service.user.UserSeviceImpl;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userSevice;
    @PostMapping("/register")
    public ResponseEntity<?> register(@Validated @RequestBody User user){
        ResponseEntity<?> response = userSevice.register(user);
        return response;
    }
}
