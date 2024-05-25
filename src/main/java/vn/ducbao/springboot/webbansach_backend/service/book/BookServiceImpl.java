package vn.ducbao.springboot.webbansach_backend.service.book;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.ducbao.springboot.webbansach_backend.dao.BookRepository;
import vn.ducbao.springboot.webbansach_backend.entity.Book;
import vn.ducbao.springboot.webbansach_backend.entity.Image;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookServiceImpl implements BookService{

    @Autowired
    private BookRepository bookRepository;

    public  final ObjectMapper objectMapper;

    public BookServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public ResponseEntity<?> save(JsonNode jsonNode) throws JsonProcessingException {
        try {
            Book book = objectMapper.treeToValue(jsonNode, Book.class);
            // neeus cos ma giam gia
            if(book.getDiscountPercent() != 0){
                int sellPrice = (int) ((int) book.getListPrice() - Math.round(book.getListPrice())/book.getDiscountPercent());
                book.setSellPrice(sellPrice);
            }
            // Lưu ảnh
            Image thumbnail = new Image();
            thumbnail.setBook(book);
            thumbnail.setDataImg(formatStringByJson(String.valueOf((jsonNode.get("thumbnail")))));
            thumbnail.setIcon(true);
            List<Image> imageList = new ArrayList<Image>();
            imageList.add(thumbnail);
            book.setImageList(imageList);
            bookRepository.save(book);
            return ResponseEntity.ok("Success!");
        }catch (Exception e){
            return  ResponseEntity.badRequest().build();
        }
    }
    public String formatStringByJson(String json){
        return  json.replace("\"", "");
    }
}
