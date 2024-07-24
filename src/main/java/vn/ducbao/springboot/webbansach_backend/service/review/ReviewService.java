package vn.ducbao.springboot.webbansach_backend.service.review;

import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.JsonNode;

public interface ReviewService {
    public ResponseEntity<?> getReview(JsonNode jsonNode);

    public ResponseEntity<?> addReview(JsonNode jsonNode);

    public ResponseEntity<?> updateReview(JsonNode jsonNode);
}
