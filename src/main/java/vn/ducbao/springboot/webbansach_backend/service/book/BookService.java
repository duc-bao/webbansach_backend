package vn.ducbao.springboot.webbansach_backend.service.book;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;

public interface BookService {
    public ResponseEntity<?> save(JsonNode jsonNode) throws JsonProcessingException;
}
