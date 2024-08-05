package vn.ducbao.springboot.webbansach_backend.service.book;

import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import vn.ducbao.springboot.webbansach_backend.dto.response.PageResponse;

public interface BookService {
    public ResponseEntity<?> save(JsonNode jsonNode) throws JsonProcessingException;

    public ResponseEntity<?> update(JsonNode jsonNode) throws JsonProcessingException;

    public long getTotalBook();

    PageResponse<?> getAll(int pageNo, int pageSize, String sortBy);

    PageResponse<?> searchElk(int pageNo, int pageSize, String sortBy, String[] filter, String keyword);

    ResponseEntity<?> getAlla();
    //    public Page<BookListResponse> getBookList(Pageable pageable);
    //    List<Book> getBook(String keyword, int categoryId, int page, int size) throws JsonProcessingException;
}
