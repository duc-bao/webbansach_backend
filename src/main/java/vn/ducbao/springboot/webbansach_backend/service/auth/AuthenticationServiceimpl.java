package vn.ducbao.springboot.webbansach_backend.service.auth;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
//import vn.ducbao.springboot.webbansach_backend.dto.request.ExchangCodeRequest;
import vn.ducbao.springboot.webbansach_backend.dto.request.JwtRequest;
//import vn.ducbao.springboot.webbansach_backend.dto.response.AuthenticationResponse;
//import vn.ducbao.springboot.webbansach_backend.dto.response.ExchangeTokenResponse;
//import vn.ducbao.springboot.webbansach_backend.dto.response.OutboundUserinfo;
import vn.ducbao.springboot.webbansach_backend.entity.Role;
import vn.ducbao.springboot.webbansach_backend.entity.User;
import vn.ducbao.springboot.webbansach_backend.repository.RoleRepository;
import vn.ducbao.springboot.webbansach_backend.repository.UserRepository;
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
    RestTemplate restTemplate;
    RoleRepository roleRepository;
    UserRepository userRepository;

    @NonFinal
    @Value("${spring.token.prefix}")
    String tokenPrefix;

    @NonFinal
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    String CLIENT_ID;

    @NonFinal
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    String CLIENT_SECRET;

    @NonFinal
    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    String REDIRECT_URI;

    @NonFinal
    String GRANT_TYPE = "authorization_code";

    String URL_EXCHANGE_TOKEN = "https://oauth2.googleapis.com/token";
    String URL_INFO = "https://www.googleapis.com/oauth2/v1/userinfo";

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

//    @Override
//    public AuthenticationResponse sosicalogin(String authencode) {
//        var response = getTokenResponse(authencode);
//        log.info("Token response {}", response);
//        var userinfo = getInfoUser("json", response.getBody().getToken());
//        Role userRole = roleRepository
//                .findByNameRole("ROLE_USER")
//                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//        var user = userRepository.findByUsername(userinfo.getBody().getEmail());
//        if (user == null) {
//            userRepository.save(User.builder()
//                    .email(userinfo.getBody().getEmail())
//                    .username(userinfo.getBody().getEmail())
//                    .roleList(List.of(userRole))
//                    .enabled(true)
//                    .build());
//        }
//        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
//        String token = jwtService.generateToken(userinfo.getBody().getEmail());
//        return AuthenticationResponse.builder().token(token).build();
//    }
//
//    private ResponseEntity<ExchangeTokenResponse> getTokenResponse(String authencode) {
//        ResponseEntity<ExchangeTokenResponse> response = restTemplate.postForEntity(
//                URL_EXCHANGE_TOKEN,
//                ExchangCodeRequest.builder()
//                        .clientId(CLIENT_ID)
//                        .clientSecret(CLIENT_SECRET)
//                        .code(authencode)
//                        .grantType(GRANT_TYPE)
//                        .redirectUri(REDIRECT_URI)
//                        .build(),
//                ExchangeTokenResponse.class);
//        return response;
//    }
//
//    private ResponseEntity<OutboundUserinfo> getInfoUser(String alt, String accestoken) {
//        String url = UriComponentsBuilder.fromHttpUrl(URL_INFO)
//                .queryParam("alt", alt)
//                .queryParam("access_token", accestoken)
//                .toUriString();
//        ResponseEntity<OutboundUserinfo> response = restTemplate.getForEntity(url, OutboundUserinfo.class);
//        return response;
//    }
}
