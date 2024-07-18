package vn.ducbao.springboot.webbansach_backend.service.cart;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.ducbao.springboot.webbansach_backend.repository.CartItemRepository;
import vn.ducbao.springboot.webbansach_backend.repository.UserRepository;
import vn.ducbao.springboot.webbansach_backend.entity.CartItem;
import vn.ducbao.springboot.webbansach_backend.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartItemServiceImpl implements CartItemService {
    private final ObjectMapper  objectMapper;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private UserRepository userRepository;
    public CartItemServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public ResponseEntity<?> save(JsonNode jsonNode) {
        try {
//            System.out.println(jsonNode.toString());
            int idUser = 0;
            List<CartItem> cartItemListData = new ArrayList<>();
            for (JsonNode jsonNode1 : jsonNode){
                //CartItem cartItemData = objectMapper.convertValue(jsonNode1, CartItem.class);
                CartItem cartItemData = objectMapper.treeToValue(jsonNode1, CartItem.class);
                //System.out.println(cartItemData.toString());
                idUser = Integer.parseInt(formatStringByJson(String.valueOf(jsonNode1.get("idUser"))));
                cartItemListData.add(cartItemData);
            }
            Optional<User> user = userRepository.findById(idUser);
            //System.out.println(user.get().getIdUser());
            // Danh sach item cuar user
            List<CartItem> cartItemList = user.get().getListCartItems();
            // Lap qua tung item va xu li
            for (CartItem cartItemData: cartItemListData){
                boolean isHad = false;
                for (CartItem cartItem: cartItemList){
                    // Nếu trong cart của user có item đó rồi thì sẽ update lại quantity
                    if(cartItem.getBook().getIdBook() == cartItemData.getBook().getIdBook()) {
                        cartItem.setQuantity(cartItem.getQuantity() + cartItemData.getQuantity());
                        isHad = true;
                        break;
                    }
                }
                // neu chua co thi them item do vao
                if(!isHad){
                    CartItem cartItem = new CartItem();
                    cartItem.setUser(user.get());
                    cartItem.setQuantity(cartItemData.getQuantity());
                    cartItem.setBook(cartItemData.getBook());
                    cartItemList.add(cartItem);
                }
            }
            user.get().setListCartItems(cartItemList);
            User newUser = userRepository.save(user.get());
            if(cartItemListData.size() == 1){
                List<CartItem> cartItemListTemp = newUser.getListCartItems();
                return ResponseEntity.ok(cartItemListTemp.get(cartItemList.size() - 1).getIdCart());
            }
            return  ResponseEntity.ok().build();

        }catch (Exception e){
            e.printStackTrace();
            return  ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<?> update(JsonNode jsonNode) {
        try{
            System.out.println(jsonNode.toString());
            int idCart = Integer.parseInt(formatStringByJson(String.valueOf(jsonNode.get("idCart"))));
            int quatity = Integer.parseInt(formatStringByJson(String.valueOf(jsonNode.get("quantity"))));
            Optional<CartItem> cartItem = cartItemRepository.findById(idCart);
            cartItem.get().setQuantity(quatity);
            cartItemRepository.save(cartItem.get());
            return  ResponseEntity.ok().build();
        }catch (Exception e){
            e.printStackTrace();
            return  ResponseEntity.badRequest().build();
        }
    }
    private String formatStringByJson(String json) {
        return json.replaceAll("\"", "");
    }
}
