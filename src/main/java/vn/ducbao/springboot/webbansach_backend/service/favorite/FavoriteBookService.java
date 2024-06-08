package vn.ducbao.springboot.webbansach_backend.service.favorite;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;
import vn.ducbao.springboot.webbansach_backend.entity.FavoriteBook;

import java.util.List;

public interface FavoriteBookService {
    public ResponseEntity<?> getFavoriteBook(Integer idUser);
    public ResponseEntity<?> save(JsonNode jsonNode);
    public ResponseEntity<?> delete(JsonNode jsonNode);
}
