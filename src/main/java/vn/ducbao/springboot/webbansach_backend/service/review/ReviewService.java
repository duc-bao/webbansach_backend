package vn.ducbao.springboot.webbansach_backend.service.review;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;

public interface ReviewService {
    public ResponseEntity<?> getReview(JsonNode jsonNode);
    public ResponseEntity<?> addReview(JsonNode jsonNode);
    public ResponseEntity<?> updateReview(JsonNode jsonNode);
}
