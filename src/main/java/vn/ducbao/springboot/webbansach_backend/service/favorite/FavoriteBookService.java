package vn.ducbao.springboot.webbansach_backend.service.favorite;

import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.JsonNode;

public interface FavoriteBookService {
    public ResponseEntity<?> getFavoriteBook(Integer idUser);

    public ResponseEntity<?> save(JsonNode jsonNode);

    public ResponseEntity<?> delete(JsonNode jsonNode);
}
