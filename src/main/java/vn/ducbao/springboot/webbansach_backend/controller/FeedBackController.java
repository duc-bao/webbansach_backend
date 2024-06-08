package vn.ducbao.springboot.webbansach_backend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.ducbao.springboot.webbansach_backend.service.feedback.FeedBackService;

@RestController
@RequestMapping("/feedback")
public class FeedBackController {
    @Autowired
    private FeedBackService feedBackService;

    @PostMapping("/add-feedback")
    public ResponseEntity<?> addFeedBack( @RequestBody JsonNode jsonNode){
        try {
            return feedBackService.addFeedBack(jsonNode);
        }catch (Exception e){
            e.printStackTrace();
            return  ResponseEntity.badRequest().build();
        }
    }
    @PutMapping("/update-feedback/{idFeedBack}")
    public ResponseEntity<?> updateFeedBack(@PathVariable Integer idFeedBack){
        try {
            return feedBackService.updateFeedBack(idFeedBack);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
