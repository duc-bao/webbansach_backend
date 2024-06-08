package vn.ducbao.springboot.webbansach_backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.ducbao.springboot.webbansach_backend.service.favorite.FavoriteBookService;

@RestController
@RequestMapping("/favorite-book")
public class FavoriteBookController {
    @Autowired
    private FavoriteBookService favoriteBookService;

    @PostMapping("/add-favorite")
    public ResponseEntity<?> saveFavoriteBook(@RequestBody JsonNode jsonNode){
        try {
            return favoriteBookService.save(jsonNode);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
    @GetMapping("/get-favorite-book/{idUser}")
    public ResponseEntity<?> getFavoriteBook(@PathVariable Integer idUser){
        try {
            return  favoriteBookService.getFavoriteBook(idUser);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
    @DeleteMapping("/delete-book")
    public  ResponseEntity<?> deleteFavorite(@RequestBody JsonNode jsonNode){
        try {
            return  favoriteBookService.delete(jsonNode);
        }catch (Exception e){
            e.printStackTrace();
            return  ResponseEntity.badRequest().build();
        }
    }
}
