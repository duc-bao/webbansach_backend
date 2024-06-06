package vn.ducbao.springboot.webbansach_backend.service.order;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;

public interface OrderService {
    public ResponseEntity<?> addOrder(JsonNode jsonNode);
}
