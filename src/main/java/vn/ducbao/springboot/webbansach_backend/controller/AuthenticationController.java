package vn.ducbao.springboot.webbansach_backend.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.ducbao.springboot.webbansach_backend.dto.request.JwtRequest;
import vn.ducbao.springboot.webbansach_backend.service.auth.AuthenticationService;
import vn.ducbao.springboot.webbansach_backend.service.user.UserService;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequestMapping("/api/user")
public class AuthenticationController {
    UserService userService;
    AuthenticationService authenticationService;
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody JwtRequest jwtRequest) {
        authenticationService.logout(jwtRequest);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody JwtRequest jwtRequest) {
        return authenticationService.refresh(jwtRequest);
    }
}
