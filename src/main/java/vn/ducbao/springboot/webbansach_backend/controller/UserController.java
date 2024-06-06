package vn.ducbao.springboot.webbansach_backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.ducbao.springboot.webbansach_backend.entity.Notification;
import vn.ducbao.springboot.webbansach_backend.entity.User;
import vn.ducbao.springboot.webbansach_backend.security.JwtResponse;
import vn.ducbao.springboot.webbansach_backend.security.LoginRequest;
import vn.ducbao.springboot.webbansach_backend.service.jwt.JwtService;
import vn.ducbao.springboot.webbansach_backend.service.user.UserService;
import vn.ducbao.springboot.webbansach_backend.service.user.UserSeviceImpl;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userSevice;
    @Autowired
    private AuthenticationManager  authenticationManager;
    @Autowired
    private JwtService jwtService;
    @PostMapping("/register")
    public ResponseEntity<?> register(@Validated @RequestBody User user){
        ResponseEntity<?> response = userSevice.register(user);
        return response;
    }
    @GetMapping("/active-account")
    public ResponseEntity<?> activeUser(@RequestParam String email, @RequestParam String activeCode){
        ResponseEntity<?> response = userSevice.activeUser(email,activeCode);
        return  response;
    }
    @PostMapping("/login")
    public ResponseEntity<?> loginPage(@RequestBody LoginRequest loginRequest){
        //Xác thực người dùng bằng username và password
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
            // Nếu xác thực thành công , tạo token JWT
            if(authentication.isAuthenticated()){
                final String jwt = jwtService.generateToken(loginRequest.getUsername());
                return ResponseEntity.ok(new JwtResponse(jwt));
            }
        }catch (AuthenticationException e){
            //Nếu xác thực không thành công
            return  ResponseEntity.badRequest().body(new Notification("Tên đăng nhập mật khẩu không chính xác"));
        }
        return  ResponseEntity.badRequest().body(new Notification("Xác thực không thành công"));
    }

    @PutMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody JsonNode jsonNode){
        try {
            return  userSevice.forgotPassword(jsonNode);
        }catch (Exception e){
            e.printStackTrace();
            return  ResponseEntity.badRequest().build();
        }
    }
    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody JsonNode jsonNode){
        System.out.println(jsonNode);
        try {
            return  userSevice.changePassword(jsonNode);
        }catch (Exception e){
            e.printStackTrace();
            return   ResponseEntity.badRequest().build();
        }
    }
    @PutMapping("/change-avatar")
    public  ResponseEntity<?> changeAvatar(@RequestBody JsonNode jsonNode){
        try {
            return  userSevice.changeAvatar(jsonNode);
        }catch (Exception e){
            e.printStackTrace();
            return  ResponseEntity.badRequest().build();
        }
    }
    @PutMapping("/update-profile")
    public ResponseEntity<?> updateProfile(@RequestBody JsonNode jsonNode){
        try {
            return  userSevice.updateProfile(jsonNode);
        }catch (Exception e){
            e.printStackTrace();
            return  ResponseEntity.badRequest().build();
        }
    }
}
