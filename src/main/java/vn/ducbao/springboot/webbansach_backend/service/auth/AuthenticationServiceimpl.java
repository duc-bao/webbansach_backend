package vn.ducbao.springboot.webbansach_backend.service.auth;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import vn.ducbao.springboot.webbansach_backend.dto.request.JwtRequest;
import vn.ducbao.springboot.webbansach_backend.security.JwtResponse;
import vn.ducbao.springboot.webbansach_backend.service.jwt.JwtService;
import vn.ducbao.springboot.webbansach_backend.service.redis.BaseRedisService;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthenticationServiceimpl implements AuthenticationService {
    JwtService jwtService;
    BaseRedisService baseRedisService;

    @NonFinal
    @Value("${spring.token.prefix}")
    String tokenPrefix;

    @Override
    public void logout(JwtRequest jwtRequest) {
        try {
            String jti = jwtService.extractIdToken(jwtRequest.getRefreshToken());
            Date expiredatetoken = jwtService.extractExpiration(jwtRequest.getRefreshToken());
            log.info("Date time {}", expiredatetoken.getTime());
            baseRedisService.set(tokenPrefix + jti, jti);
            baseRedisService.setTimeToLive(tokenPrefix + jti, expiredatetoken.getTime());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> refresh(JwtRequest jwtRequest) {
        try {
            if (jwtService.validateTokenLogout(jwtRequest.getRefreshToken())) {
                String jti = jwtService.extractIdToken(jwtRequest.getRefreshToken());
                if (baseRedisService.get(tokenPrefix + jti) == null) {
                    String username = jwtService.extractUsername(jwtRequest.getRefreshToken());
                    String token = jwtService.generateToken(username);
                    return ResponseEntity.ok(new JwtResponse(token, jwtRequest.getRefreshToken()));
                }
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalied");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
