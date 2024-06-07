package vn.ducbao.springboot.webbansach_backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.ducbao.springboot.webbansach_backend.service.order.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/add-order")
    public ResponseEntity<?> addOrder(@RequestBody JsonNode jsonNode){
        try {
            return  orderService.addOrder(jsonNode);
        }catch (Exception e){
            e.printStackTrace();
            return  ResponseEntity.badRequest().build();
        }
    }
    @PutMapping("/update-order")
    public  ResponseEntity<?> updateOrder(@RequestBody JsonNode jsonNode){
        try {
            return  orderService.updateOrder(jsonNode);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
    @PutMapping("/cancel-order") // khi thanh toán mà huỷ thanh toán
    public  ResponseEntity<?> cancelOrder(@RequestBody JsonNode jsonNode){
        try {
            return  orderService.cancel(jsonNode);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
