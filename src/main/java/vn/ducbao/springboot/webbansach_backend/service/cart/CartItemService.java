package vn.ducbao.springboot.webbansach_backend.service.cart;

import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.JsonNode;

public interface CartItemService {
    public ResponseEntity<?> save(JsonNode jsonNode);

    public ResponseEntity<?> update(JsonNode jsonNode);
}
