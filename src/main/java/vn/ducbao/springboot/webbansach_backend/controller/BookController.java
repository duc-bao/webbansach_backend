package vn.ducbao.springboot.webbansach_backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.ducbao.springboot.webbansach_backend.service.book.BookService;

@RestController
@RequestMapping("/book")
public class BookController {
    @Autowired
    private BookService bookService;

    @PostMapping(path = "add-book")
    public ResponseEntity<?> save(@RequestBody JsonNode jsonNode){
        try {
            return bookService.save(jsonNode);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("looi");
            return  ResponseEntity.badRequest().build();
        }
    }
    @PostMapping(path = "update-book")
    public ResponseEntity<?> update(@RequestBody JsonNode jsonNode){
        try{
            return bookService.update(jsonNode);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("loi");
            return  ResponseEntity.badRequest().build();
        }
    }


}
