package vn.ducbao.springboot.webbansach_backend.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import vn.ducbao.springboot.webbansach_backend.service.UserSecurityService;
import vn.ducbao.springboot.webbansach_backend.service.jwt.JwtFilter;

@Configuration
public class SecurityConfiguration {
    @Autowired
    private JwtFilter auFilter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Này để cấu hình xem security sẽ làm những gì
    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserSecurityService userService) {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Cấu hình phân quyền cho endpoint
        http.authorizeHttpRequests(config -> config.requestMatchers(HttpMethod.GET, Endpoints.PUBLIC_GET_ENDPOINT)
                .permitAll()
                .requestMatchers(HttpMethod.POST, Endpoints.PUBLIC_POST_ENDPOINT)
                .permitAll()
                .requestMatchers(HttpMethod.PUT, Endpoints.PUBLIC_PUT_ENDPOINT)
                .permitAll()
                .requestMatchers(HttpMethod.DELETE, Endpoints.PUBLIC_DELETE_ENDPOINT)
                .permitAll()
                .requestMatchers(HttpMethod.GET, Endpoints.ADMIN_ENDPOINT)
                .hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.POST, Endpoints.ADMIN_ENDPOINT)
                .hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT, Endpoints.ADMIN_ENDPOINT)
                .hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE, Endpoints.ADMIN_ENDPOINT)
                .hasAuthority("ADMIN"));
        // Cấu hình cors
        http.cors(cors -> {
            cors.configurationSource(request -> {
                CorsConfiguration corsConfig = new CorsConfiguration();
                corsConfig.addAllowedOrigin(Endpoints.endpoint_fe);
                corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
                corsConfig.addAllowedHeader("*");
                return corsConfig;
            });
        });
        // Thêm lọc trước khi thêm vào
        http.addFilterBefore(auFilter, UsernamePasswordAuthenticationFilter.class);
        // Cấu hình session không lưu trữ trạng thái
        http.sessionManagement((seesion) -> seesion.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.httpBasic(Customizer.withDefaults());
        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    // Tạo authentication để JWT có thể quản lí
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
