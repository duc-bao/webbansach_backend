package vn.ducbao.springboot.webbansach_backend.service.review;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.ducbao.springboot.webbansach_backend.dao.*;
import vn.ducbao.springboot.webbansach_backend.entity.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private ReviewRepository reviewRepository;

    public ReviewServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public ResponseEntity<?> getReview(JsonNode jsonNode) {
        try {
            int idOrder = Integer.parseInt(formatStringByJson(String.valueOf(jsonNode.get("idOrder"))));
            int idBook = Integer.parseInt(formatStringByJson(String.valueOf(jsonNode.get("idBook"))));
            Order order = orderRepository.findById(idOrder).get();
            Book book = bookRepository.findById(idBook).get();
            List<OrderDetail> orderDetailList = orderDetailRepository.findOrderDetailsByOrder(order);
            for (OrderDetail orderDetail : orderDetailList) {
                if (orderDetail.getBook().getIdBook() == book.getIdBook()) {
                    Review review = reviewRepository.findReviewByOrderDetail(orderDetail);
                    Review reviewReponse = new Review();// Trả review luôn bị lỗi không được, nên phải dùng cách này
                    reviewReponse.setIdReview(review.getIdReview());
                    reviewReponse.setComment(review.getComment());
                    reviewReponse.setDateCreated(review.getDateCreated());
                    reviewReponse.setRatingPoint(review.getRatingPoint());
                    return ResponseEntity.status(HttpStatus.OK).body(reviewReponse);
                }
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().build();
    }

    @Override
    public ResponseEntity<?> addReview(JsonNode jsonNode) {
        try {
            int idUser = Integer.parseInt(formatStringByJson(String.valueOf(jsonNode.get("idUser"))));
            int idOrder = Integer.parseInt(formatStringByJson(String.valueOf(jsonNode.get("idOrder"))));
            int idBook = Integer.parseInt(formatStringByJson(String.valueOf(jsonNode.get("idBook"))));
            float ratingValue = Float.parseFloat(formatStringByJson(String.valueOf(jsonNode.get("ratingPoint"))));
            String content = formatStringByJson(String.valueOf(jsonNode.get("content")));
            User user = userRepository.findById(idUser).get();
            Order order = orderRepository.findById(idOrder).get();
            List<OrderDetail> orderDetailList = orderDetailRepository.findOrderDetailsByOrder(order);
            Book book = bookRepository.findById(idBook).get();

            for (OrderDetail orderDetail : orderDetailList) {
                if (orderDetail.getBook().getIdBook() == book.getIdBook()) {
                    orderDetail.setReview(true);
                    Review review = new Review();
                    review.setBook(book);
                    review.setUser(user);
                    review.setComment(content);
                    review.setRatingPoint(ratingValue);
                    review.setOrderDetail(orderDetail);
                    // Layas thoi gian hien tai
                    Instant instant = Instant.now();
                    // Chuyen thanh timestamp
                    Timestamp timestamp = Timestamp.from(instant);
                    review.setDateCreated(timestamp);
                    orderDetailRepository.save(orderDetail);
                    reviewRepository.save(review);
                    break;
                }
            }
            // Set lại rating trung bình của quyển sách đó
            List<Review> reviewList  = reviewRepository.findAll();
            double sum = 0;// Tổng rating
            int n = 0;// Số lượng rating
            for(Review review : reviewList){
                if(review.getBook().getIdBook() == idBook){
                    n++;
                    sum+=review.getRatingPoint();
                }
            }
            double ratingAvg = sum / n;
            book.setAvgRating(ratingAvg);
            bookRepository.save(book);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();

    }

    @Override
    public ResponseEntity<?> updateReview(JsonNode jsonNode) {
     try {
            Review reviewResponese  = objectMapper.treeToValue(jsonNode, Review.class);
            Review review = reviewRepository.findById(reviewResponese.getIdReview()).get();
            review.setComment(reviewResponese.getComment());
            review.setRatingPoint(reviewResponese.getRatingPoint());
            reviewRepository.save(review);
     }catch (Exception e){
         e.printStackTrace();
         return ResponseEntity.badRequest().build();
     }
        return ResponseEntity.ok().build();
    }

    private String formatStringByJson(String json) {
        return json.replaceAll("\"", "");
    }
}
