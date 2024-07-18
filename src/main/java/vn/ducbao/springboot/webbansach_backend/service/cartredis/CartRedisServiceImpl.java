package vn.ducbao.springboot.webbansach_backend.service.cartredis;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vn.ducbao.springboot.webbansach_backend.dto.request.CartItemRequest;
import vn.ducbao.springboot.webbansach_backend.dto.response.CartItemResponse;
import vn.ducbao.springboot.webbansach_backend.service.redis.BaseRedisService;

import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CartRedisServiceImpl implements CartRedisService {
    BaseRedisService baseRedisService;
    @NonFinal
    @Value("${spring.cart.id.prefix}")
    String cartIdPrefix;
    @NonFinal
    @Value("${spring.product.cart.id}")
    String ID_PRODUCT;
    @NonFinal
    @Value("${spring.TLL.cart}")
    long CART_TIME_OUT;
    @Override
    public void addtoCart(int userId, List<CartItemRequest> cartItems) {
        String fieldKey;
        String userid = String.valueOf(userId);
        int updateQuantity;
        for(CartItemRequest cartItem : cartItems) {
            fieldKey = ID_PRODUCT + cartItem.getProductId();
            if(baseRedisService.hashExists(userid, fieldKey)){
                updateQuantity =(Integer)baseRedisService.hashGet(userid, fieldKey) + cartItem.getQuantity();
            }else {
                updateQuantity = cartItem.getQuantity();
            }
            baseRedisService.hashSet(userid, fieldKey, updateQuantity);
        }
        baseRedisService.setTimeToLive(userid, CART_TIME_OUT);
    }

    @Override
    public void updateCart(int userId, List<CartItemRequest> cartItems) {
        String fieldKey;
        String userid = String.valueOf(userId);
//        List<String> fieldsKey = new ArrayList<>();
        for(CartItemRequest cartItem : cartItems) {
            fieldKey = ID_PRODUCT + cartItem.getProductId();
//            fieldsKey.add(fieldKey);
            baseRedisService.delete(userid, fieldKey);
            baseRedisService.hashSet(userid, fieldKey, cartItem.getQuantity());
        }
        //baseRedisService.delete(userid, fieldsKey);
        baseRedisService.setTimeToLive(userid, CART_TIME_OUT);
    }

    @Override
    public void deleteProductInCart(int userId, CartItemRequest cartItemRequest){
        String fieldKey = ID_PRODUCT + cartItemRequest.getProductId();
        String userid = String.valueOf(userId);
        baseRedisService.delete(userid, fieldKey);
    }

    @Override
    public List<CartItemResponse> getProductsInCart(int userId) {
        String userid = String.valueOf(userId);
        Map<String, Object> product = baseRedisService.getField(userid);
        return convertToCartItemResponseList(product);
    }

    private List<CartItemResponse> convertToCartItemResponseList(Map<String, Object> product) {
        List<CartItemResponse> cartItemResponses = new ArrayList<>();
        for (Map.Entry<String, Object> entry : product.entrySet()) {
            CartItemResponse response = new CartItemResponse();
            String[] parts = entry.getKey().split(":");
            int idProduct = Integer.parseInt(parts[1]);
            response.setIdProduct(idProduct);
            response.setQuantity((Integer) entry.getValue());
            cartItemResponses.add(response);
        }
        return cartItemResponses;
    }


}
