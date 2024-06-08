package vn.ducbao.springboot.webbansach_backend.service.feedback;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;

public interface FeedBackService {
    public ResponseEntity<?> addFeedBack(JsonNode jsonNode);
}
