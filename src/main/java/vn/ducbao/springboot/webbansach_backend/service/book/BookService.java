package vn.ducbao.springboot.webbansach_backend.service.book;

import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

public interface BookService {
    public ResponseEntity<?> save(JsonNode jsonNode) throws JsonProcessingException;

    public ResponseEntity<?> update(JsonNode jsonNode) throws JsonProcessingException;

    public long getTotalBook();
    //    public Page<BookListResponse> getBookList(Pageable pageable);
    //    List<Book> getBook(String keyword, int categoryId, int page, int size) throws JsonProcessingException;
}
