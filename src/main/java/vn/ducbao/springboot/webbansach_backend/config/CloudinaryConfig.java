package vn.ducbao.springboot.webbansach_backend.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

@Configuration
public class CloudinaryConfig {
    private final String CLOUD_NAME = "CLOUD_NAME_YOUR";
    private final String API_KEY = "CLOUD_API_KEY_YOUR";
    private final String API_SECRET = "CLOUD_API_SECRET_YOUR";

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", CLOUD_NAME);
        config.put("api_key", API_KEY);
        config.put("api_secret", API_SECRET);
        return new Cloudinary(config);
    }
}
