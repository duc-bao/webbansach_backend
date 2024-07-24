package vn.ducbao.springboot.webbansach_backend.service.cartredis;

import java.util.List;

import vn.ducbao.springboot.webbansach_backend.dto.request.CartItemRequest;
import vn.ducbao.springboot.webbansach_backend.dto.response.CartItemResponse;

public interface CartRedisService {
    void addtoCart(int customerId, List<CartItemRequest> cartItems);

    void updateCart(int customerId, List<CartItemRequest> cartItems, int cartid);

    void deleteProductInCart(int customerId, CartItemRequest cartItemRequest);

    List<CartItemResponse> getProductsInCart(int customerId);
}
