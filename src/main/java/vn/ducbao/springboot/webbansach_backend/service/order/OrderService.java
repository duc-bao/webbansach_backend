package vn.ducbao.springboot.webbansach_backend.service.order;

import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.JsonNode;

public interface OrderService {
    public ResponseEntity<?> addOrder(JsonNode jsonNode);

    public ResponseEntity<?> updateOrder(JsonNode jsonNode);

    public ResponseEntity<?> cancel(JsonNode jsonNode);
}
