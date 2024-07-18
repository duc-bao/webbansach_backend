package vn.ducbao.springboot.webbansach_backend.service.book;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import vn.ducbao.springboot.webbansach_backend.dto.response.BookListResponse;
import vn.ducbao.springboot.webbansach_backend.entity.Book;

import java.util.List;

public interface BookService {
    public ResponseEntity<?> save(JsonNode jsonNode) throws JsonProcessingException;
    public ResponseEntity<?> update(JsonNode jsonNode) throws  JsonProcessingException;
    public long getTotalBook();
//    public Page<BookListResponse> getBookList(Pageable pageable);
//    List<Book> getBook(String keyword, int categoryId, int page, int size) throws JsonProcessingException;
}
