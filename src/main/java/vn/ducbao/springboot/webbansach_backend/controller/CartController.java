package vn.ducbao.springboot.webbansach_backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.ducbao.springboot.webbansach_backend.service.cart.CartItemService;

@RestController
@RequestMapping("/cart-item")
public class CartController {
    @Autowired
    private CartItemService cartItemService;

    @PostMapping("/add-cart")
    public ResponseEntity<?> save(@RequestBody JsonNode jsonNode){
        try{
            return cartItemService.save(jsonNode);
        }catch (Exception e){
            e.printStackTrace();
            return  ResponseEntity.badRequest().build();
        }
    }
    @PutMapping("/update-cart")
    public ResponseEntity<?> update(@RequestBody JsonNode jsonNode){
        try {
            return  cartItemService.update(jsonNode);
        }catch (Exception e){
            e.printStackTrace();
            return  ResponseEntity.badRequest().build();
        }
    }

}
