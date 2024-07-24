package vn.ducbao.springboot.webbansach_backend.service.cartredis;

import vn.ducbao.springboot.webbansach_backend.dto.request.CartItemRequest;
import vn.ducbao.springboot.webbansach_backend.dto.response.CartItemResponse;

import java.util.List;
import java.util.Map;

public interface CartRedisService {
    void addtoCart(int customerId, List<CartItemRequest> cartItems);
    void updateCart(int customerId, List<CartItemRequest> cartItems, int cartid);
    void deleteProductInCart(int customerId, CartItemRequest cartItemRequest);
    List<CartItemResponse> getProductsInCart(int customerId);
}
