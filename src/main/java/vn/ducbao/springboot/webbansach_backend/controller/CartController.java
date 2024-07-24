package vn.ducbao.springboot.webbansach_backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.ducbao.springboot.webbansach_backend.dto.request.CartItemRequest;
import vn.ducbao.springboot.webbansach_backend.dto.response.CartItemResponse;
import vn.ducbao.springboot.webbansach_backend.service.cart.CartItemService;
import vn.ducbao.springboot.webbansach_backend.service.cartredis.CartRedisService;

import java.util.List;

@RestController
@RequestMapping("/cart-item")
public class CartController {
    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private CartRedisService cartRedisService;

    @PostMapping("/add/{id}")
    public ResponseEntity<?> addCartItem(@PathVariable int id, @RequestBody List<CartItemRequest> cartItemRequest) {
        cartRedisService.addtoCart(id, cartItemRequest);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/update/{idcart}/{id}")
    public ResponseEntity<?> updateCart( @PathVariable(value = "id") int id, @RequestBody List<CartItemRequest> cartItemRequest,@PathVariable(value = "idcart") int idcart){
        cartRedisService.updateCart(id, cartItemRequest, idcart);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCart(@PathVariable int userId, @RequestBody CartItemRequest cartItemRequest){
         cartRedisService.deleteProductInCart(userId, cartItemRequest);
         return ResponseEntity.ok().build();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<List<CartItemResponse>> getCartItems(@PathVariable int id) {
        List<CartItemResponse> cartItems = cartRedisService.getProductsInCart(id);
        return ResponseEntity.ok(cartItems);
    }
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
