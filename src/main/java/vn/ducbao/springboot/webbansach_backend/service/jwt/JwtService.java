package vn.ducbao.springboot.webbansach_backend.service.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import vn.ducbao.springboot.webbansach_backend.entity.User;
import vn.ducbao.springboot.webbansach_backend.service.user.UserService;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtService {
    public static final String SECRET = "MTIzNDU2NDU5OThEMzIxM0F6eGMzNTE2NTQzMjEzMjE2NTQ5OHEzMTNhMnMxZDMyMnp4M2MyMQ==";
    @Autowired
    private UserService userService;

    //Tạo JWT dựa trên username(tạo thông tin cần trả về cho FE khi đăng nhập thành công)
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();

//        claims.put("isAdmin", true);
        return createToken(claims, username);
    }

    // Tạo JWT với các claims đã cho
    private String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+30*60*1000)) //JWT hết hạn sau 30p
                .signWith(SignatureAlgorithm.HS256, getSignKey())
                .compact();
    }
    //Lấy select key mã hóa base64
    private Key getSignKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    // Trích xuất thông tin
    private Claims extractAllClaims(String token){
        return Jwts.parser().setSigningKey(getSignKey()).parseClaimsJws(token).getBody();
    }
    // Trichs xuất thông tin cho 1 claims
    private  <T> T extractClaim(String token, Function<Claims, T> claimsTFunction){
        final Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }
    // Trichs xuất thông số thời gian hết hạn từ JWT
    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }
    // trích xuất thông số username
    public String extractUsername(String token){
        return  extractClaim(token, Claims::getSubject);
    }
    // kiểm tra token đã hết hạn hay chưa
    private Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }
    // Kiểm tra tính hợp lệ của token
    public  Boolean validateToken(String token, UserDetails userDetails ){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


}
