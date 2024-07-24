package vn.ducbao.springboot.webbansach_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.JsonNode;

import vn.ducbao.springboot.webbansach_backend.service.review.ReviewService;

@RestController
@RequestMapping("/review")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @GetMapping("/get-review")
    public ResponseEntity<?> getReview(@RequestBody JsonNode jsonNode) {
        try {
            return reviewService.getReview(jsonNode);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/add-review")
    public ResponseEntity<?> addReview(@RequestBody JsonNode jsonNode) {
        try {
            return reviewService.addReview(jsonNode);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update-review")
    public ResponseEntity<?> updateReview(@RequestBody JsonNode jsonNode) {
        try {
            return reviewService.updateReview(jsonNode);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
