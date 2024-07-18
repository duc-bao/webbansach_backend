package vn.ducbao.springboot.webbansach_backend.service.jwt;

import com.auth0.jwt.JWT;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import vn.ducbao.springboot.webbansach_backend.entity.Role;
import vn.ducbao.springboot.webbansach_backend.entity.User;
import vn.ducbao.springboot.webbansach_backend.service.UserSecurityService;
import vn.ducbao.springboot.webbansach_backend.service.user.UserService;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Component
public class JwtService {
    @Value("${spring.token.key}")
    private   String SECRET;
    @Value("${spring.token.expireToken}")
    private long expireExpiraToken;
    @Value("${spring.token.expireRefreshToken}")
    private long expireRefreshToken;
    @Autowired
    private UserSecurityService userService;
    Logger logger = LoggerFactory.getLogger(JwtService.class);
    //Tạo JWT dựa trên username(tạo thông tin cần trả về cho FE khi đăng nhập thành công)
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        User user = userService.findByUsername(username);
        claims.put("id", user.getIdUser());
        claims.put("avatar", user.getAvatar());
        claims.put("lastName", user.getLastName());
        claims.put("enabled", user.isEnabled());
        claims.put("jti", UUID.randomUUID().toString());
        List<Role> list = user.getRoleList();
        if (user != null && user.getRoleList().size() >= 0) {
            for (Role r : list
            ) {
                if(r.getNameRole().equals("ADMIN")){
                    claims.put("role", "ADMIN");
                    break;
                }
                if(r.getNameRole().equals("STAFF")){
                    claims.put("role", "STAFF");
                    break;
                }
                if(r.getNameRole().equals("CUSTOMER")){
                    claims.put("role", "CUSTOMER");
                    break;
                }
            }
        }
        //        claims.put("isAdmin", true);
        return createToken(claims, username);
    }
    public  String generateRefreshToken(String username) {
        User user = userService.findByUsername(username);
        Map<String, Object> claims = new HashMap<>();
        claims.put("jti", UUID.randomUUID().toString());
        return createRefreshToken(claims, username);
    }
    private  String createRefreshToken(Map<String, Object> claims, String username) {
        return  Jwts.builder().setClaims(claims).setSubject(username).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireRefreshToken))
                .signWith(SignatureAlgorithm.HS256, getSignKey()).compact();
    }
    public String extractIdToken(String token) {
        return  extractClaim(token, claims -> claims.get("jti").toString());
    }
    // Tạo JWT với các claims đã cho
    private String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireExpiraToken)) //JWT hết hạn sau 30p
                .signWith(SignatureAlgorithm.HS256, getSignKey())
                .compact();
    }

    //Lấy select key mã hóa base64
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Trích xuất thông tin
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(getSignKey()).parseClaimsJws(token).getBody();
    }

    // Trichs xuất thông tin cho 1 claims
    private <T> T extractClaim(String token, Function<Claims, T> claimsTFunction) {
        final Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    // Trichs xuất thông số thời gian hết hạn từ JWT
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // trích xuất thông số username
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // kiểm tra token đã hết hạn hay chưa
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    public boolean validateTokenLogout(String token) {
        try {
            Jwts.parser().setSigningKey(getSignKey()).parse(token);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    // Kiểm tra tính hợp lệ của token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


}
