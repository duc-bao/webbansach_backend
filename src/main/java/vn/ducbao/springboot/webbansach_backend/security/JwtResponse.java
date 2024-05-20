package vn.ducbao.springboot.webbansach_backend.security;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


public class JwtResponse {
    private final String jwt;

    public JwtResponse(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }
}
