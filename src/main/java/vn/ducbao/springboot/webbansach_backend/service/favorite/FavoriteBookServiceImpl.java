package vn.ducbao.springboot.webbansach_backend.service.favorite;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.ducbao.springboot.webbansach_backend.repository.BookRepository;
import vn.ducbao.springboot.webbansach_backend.repository.FavoriteBookRepository;
import vn.ducbao.springboot.webbansach_backend.repository.UserRepository;
import vn.ducbao.springboot.webbansach_backend.entity.Book;
import vn.ducbao.springboot.webbansach_backend.entity.FavoriteBook;
import vn.ducbao.springboot.webbansach_backend.entity.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class FavoriteBookServiceImpl implements FavoriteBookService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private FavoriteBookRepository favoriteBookRepository;

    @Override
    public ResponseEntity<?> getFavoriteBook(Integer idUser) {
       try {
           User user = userRepository.findById(idUser).get();
           List<FavoriteBook> favoriteBookList = favoriteBookRepository.findFavoriteBookByUser(user);
           List<Integer> idFavoriteBook = new ArrayList<>();
           for (FavoriteBook favoriteBook : favoriteBookList){
               idFavoriteBook.add(favoriteBook.getBook().getIdBook());
           }
           return ResponseEntity.ok().body(idFavoriteBook);
       }catch (Exception e){
           e.printStackTrace();
           return  ResponseEntity.badRequest().build();
       }
    }

    @Override
    public ResponseEntity<?> save(JsonNode jsonNode) {
        try {
            int idUser = Integer.parseInt(formatStringToJson(String.valueOf(jsonNode.get("idUser"))));
            int idBook = Integer.parseInt(formatStringToJson(String.valueOf(jsonNode.get("idBook"))));
            User user = userRepository.findById(idUser).get();
            Book book = bookRepository.findById(idBook).get();
            FavoriteBook favoriteBook  = FavoriteBook.builder().book(book).user(user).build();
            favoriteBookRepository.save(favoriteBook);

            return  ResponseEntity.ok().build();
        }catch (Exception e){
            e.printStackTrace();
            return  ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<?> delete(JsonNode jsonNode) {

        try {
            int idUser = Integer.parseInt(formatStringToJson(String.valueOf(jsonNode.get("idUser"))));
            int idBook = Integer.parseInt(formatStringToJson(String.valueOf(jsonNode.get("idBook"))));
            User user = userRepository.findById(idUser).get();
            Book book = bookRepository.findById(idBook).get();
            FavoriteBook favoriteBook = favoriteBookRepository.findFavoriteBookByBookAndUser(book, user);
            favoriteBookRepository.delete(favoriteBook);
            return  ResponseEntity.ok().build();
        }catch (Exception e){
            e.printStackTrace();
            return  ResponseEntity.badRequest().build();
        }
    }

    public String formatStringToJson(String json){
        return  json.replace("\"", "");
    }

}
