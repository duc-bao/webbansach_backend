package vn.ducbao.springboot.webbansach_backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.ducbao.springboot.webbansach_backend.dto.request.JwtRequest;
import vn.ducbao.springboot.webbansach_backend.service.auth.AuthenticationService;
import vn.ducbao.springboot.webbansach_backend.service.user.UserService;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Tag(name = "API Authentication")
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
    //    @PostMapping("/outbound/authentication")
    //    public ApiResponse<AuthenticationResponse> outboundAuthentication(@RequestParam(value = "code") String
    // authencode) {
    //        var result = authenticationService.sosicalogin(authencode);
    //        return ApiResponse.<AuthenticationResponse>builder().data(result).build();
    //    }

}
