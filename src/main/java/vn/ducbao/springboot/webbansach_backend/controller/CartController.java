package vn.ducbao.springboot.webbansach_backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.ducbao.springboot.webbansach_backend.service.cart.CartItemService;

@RestController
@RequestMapping("cart-item")
public class CartController {
    @Autowired
    private CartItemService cartItemService;

    @PostMapping(path = "add-cart")
    public ResponseEntity<?> save(@RequestBody JsonNode jsonNode){
        try {
            return cartItemService.save(jsonNode);
        }catch (Exception e){
            e.printStackTrace();
            return  ResponseEntity.badRequest().build();
        }
    }

}
