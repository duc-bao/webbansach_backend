package vn.ducbao.springboot.webbansach_backend.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.JsonNode;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import vn.ducbao.springboot.webbansach_backend.dto.response.BookListResponse;
import vn.ducbao.springboot.webbansach_backend.dto.response.PageResponse;
import vn.ducbao.springboot.webbansach_backend.repository.elk.BookElkRepository;
import vn.ducbao.springboot.webbansach_backend.service.book.BookService;

@RestController
@RequestMapping("/book")
@Tag(name = "API BOOk")
@Slf4j
public class BookController {
    private static final Logger log = LoggerFactory.getLogger(BookController.class);

    @Autowired
    private BookService bookService;

    @Autowired
    private BookElkRepository bookElkRepository;

    @GetMapping("/search-elk")
    public PageResponse<?> searchBook(
            @RequestParam(defaultValue = "10", required = false, value = "pageSize") int pageSize,
            @RequestParam(defaultValue = "0", required = false, value = "pageNo") int pageNo,
            @RequestParam(required = false, value = "sortBy", defaultValue = "idBook") String sortBy,
            @RequestParam(required = false, value = "keyword") String search,
            @RequestParam(required = false, value = "filter") String[] filter) {
        log.warn("Đã vào search này ");
        return bookService.searchElk(pageNo, pageSize, sortBy, filter, search);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return bookService.getAlla();
    }

    @PostMapping(path = "add-book")
    public ResponseEntity<?> save(@RequestBody JsonNode jsonNode) {
        try {
            return bookService.save(jsonNode);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("looi");
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(path = "update-book")
    public ResponseEntity<?> update(@RequestBody JsonNode jsonNode) {
        try {
            return bookService.update(jsonNode);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("loi");
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("")
    public PageResponse<?> getAll(
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
            @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy) {
        return bookService.getAll(pageNo, pageSize, sortBy);
    }

    //    @GetMapping("/search-by-criteria")
    //    public ResponseEntity<?> getSearch(@RequestParam(value = "pageSize") int pageSize, @RequestParam(value =
    // "pageNo") int pageNo
    //    , @RequestParam(value = "sortBy", defaultValue = "id", required = false) String sortBy, @RequestParam(required
    // = false) String... search){
    //        return  bookService.searchByCriteria(pageSize,pageNo,sortBy,search);
    //    }
    @GetMapping("/get-total")
    public long getTotal() {
        return bookService.getTotalBook();
    }

    @KafkaListener(topics = "async_book")
    public void listen(BookListResponse bookListResponse) {
        log.info("đồng bộ dữ liệu lên elk");
        bookElkRepository.save(bookListResponse);
    }
}
