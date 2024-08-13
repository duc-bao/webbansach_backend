package vn.ducbao.springboot.webbansach_backend.service.auth;

import org.springframework.http.ResponseEntity;

import vn.ducbao.springboot.webbansach_backend.dto.request.JwtRequest;
import vn.ducbao.springboot.webbansach_backend.dto.response.AuthenticationResponse;

public interface AuthenticationService {

    public void logout(JwtRequest jwtRequest);

    ResponseEntity<?> refresh(JwtRequest jwtRequest);

//    AuthenticationResponse sosicalogin(String authencode);
}
