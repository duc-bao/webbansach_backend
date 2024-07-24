package vn.ducbao.springboot.webbansach_backend.service.order;

import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import vn.ducbao.springboot.webbansach_backend.entity.*;
import vn.ducbao.springboot.webbansach_backend.repository.*;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    private final ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private DeliveryReponsitory deliveryReponsitory;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private BookRepository bookRepository;

    public OrderServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public ResponseEntity<?> addOrder(JsonNode jsonNode) {
        try {
            System.out.println("Received JSON: " + jsonNode.toString());
            Order orderData = objectMapper.treeToValue(jsonNode, Order.class);
            orderData.setTotalPrice(orderData.getTotalPriceProduct());
            orderData.setDateOrder(orderData.getDateOrder());
            orderData.setStatus("Đang xử lí");
            int idUser = Integer.parseInt(formatStringByJson(String.valueOf(jsonNode.get("idUser"))));
            Optional<User> user = userRepository.findById(idUser);
            orderData.setUser(user.get());
            int idDelivery = Integer.parseInt(formatStringByJson(String.valueOf(jsonNode.get("idDelivery"))));
            Optional<Delivery> delivery = deliveryReponsitory.findById(idDelivery);
            orderData.setDelivery(delivery.get());
            int idPayment = Integer.parseInt(formatStringByJson(String.valueOf(jsonNode.get("idPayment"))));
            Optional<Payment> payment = paymentRepository.findById(idPayment);
            orderData.setPayment(payment.get());
            Order newOrder = orderRepository.save(orderData);
            JsonNode jsonNodeOrder = jsonNode.get("book");
            for (JsonNode jsonNode1 : jsonNodeOrder) {
                int quatity = Integer.parseInt(formatStringByJson(String.valueOf(jsonNode1.get("quantity"))));
                Book bookRespone = objectMapper.treeToValue(jsonNode1.get("book"), Book.class);
                Optional<Book> book = bookRepository.findById(bookRespone.getIdBook());
                book.get().setQuantity(bookRespone.getQuantity() - quatity);
                book.get().setSoldQuantity(bookRespone.getSoldQuantity() + quatity);
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setQuantity(quatity);
                orderDetail.setBook(book.get());
                orderDetail.setOrder(orderData);
                orderDetail.setPrice(quatity * book.get().getSellPrice());
                orderDetail.setReview(false);
                orderDetailRepository.save(orderDetail);
                bookRepository.save(book.get());
            }
            cartItemRepository.deleteCartItemByIdUser(idUser);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> updateOrder(JsonNode jsonNode) {
        try {
            int idOrder = Integer.parseInt(formatStringByJson(String.valueOf(jsonNode.get("idOrder"))));
            String status = formatStringByJson(String.valueOf(jsonNode.get("status")));
            Optional<Order> order = orderRepository.findById(idOrder);
            order.get().setStatus(status);
            // Lấy ra order detail
            if (status.equals("Bị hủy")) {
                List<OrderDetail> orderDetailList = orderDetailRepository.findOrderDetailsByOrder(order.get());
                for (OrderDetail orderDetail : orderDetailList) {
                    Book bookResponse = orderDetail.getBook();
                    bookResponse.setQuantity(bookResponse.getQuantity() - orderDetail.getQuantity());
                    bookResponse.setSoldQuantity(bookResponse.getSoldQuantity() + orderDetail.getQuantity());
                    bookRepository.save(bookResponse);
                }
            }
            orderRepository.save(order.get());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<?> cancel(JsonNode jsonNode) {
        try {
            int idUser = Integer.parseInt(formatStringByJson(String.valueOf(jsonNode.get("idUser"))));
            User user = userRepository.findById(idUser).get();
            Order order = orderRepository.findFirstByUserOrderByIdOrderDesc(user);
            order.setStatus("Bị hủy");
            List<OrderDetail> orderDetailList = orderDetailRepository.findOrderDetailsByOrder(order);
            for (OrderDetail orderDetail : orderDetailList) {
                Book bookOrderDetail = orderDetail.getBook();
                bookOrderDetail.setSoldQuantity(bookOrderDetail.getSoldQuantity() - orderDetail.getQuantity());
                bookOrderDetail.setQuantity(bookOrderDetail.getQuantity() + orderDetail.getQuantity());
                bookRepository.save(bookOrderDetail);
            }
            orderRepository.save(order);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    private String formatStringByJson(String json) {
        return json.replaceAll("\"", "");
    }
}
