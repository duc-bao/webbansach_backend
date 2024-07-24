package vn.ducbao.springboot.webbansach_backend.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponse {
    private String token;
    private String refreshToken;

    public JwtResponse(String token) {
        this.token = token;
    }
}
