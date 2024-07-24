package vn.ducbao.springboot.webbansach_backend.service.feedback;

import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.JsonNode;

public interface FeedBackService {
    public ResponseEntity<?> addFeedBack(JsonNode jsonNode);

    public ResponseEntity<?> updateFeedBack(Integer idFeedbac);
}
