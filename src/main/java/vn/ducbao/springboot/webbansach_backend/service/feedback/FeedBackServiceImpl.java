package vn.ducbao.springboot.webbansach_backend.service.feedback;

import java.sql.Date;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import vn.ducbao.springboot.webbansach_backend.entity.FeedBack;
import vn.ducbao.springboot.webbansach_backend.entity.User;
import vn.ducbao.springboot.webbansach_backend.repository.FeedBackRepository;
import vn.ducbao.springboot.webbansach_backend.repository.UserRepository;

@Service
public class FeedBackServiceImpl implements FeedBackService {
    @Autowired
    private UserRepository userRepository;

    private final ObjectMapper objectMapper;

    @Autowired
    private FeedBackRepository feedBackRepository;

    public FeedBackServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ResponseEntity<?> addFeedBack(JsonNode jsonNode) {
        try {
            User user = userRepository.findByUsername(formatStringByJson(String.valueOf(jsonNode.get("user"))));
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
            Instant instant = Instant.from(
                    dateTimeFormatter.parse(formatStringByJson(String.valueOf(jsonNode.get("dateCreated")))));
            Date dateCreate = new Date(Date.from(instant).getTime());
            FeedBack feedBack = FeedBack.builder()
                    .title(formatStringByJson(String.valueOf(jsonNode.get("title"))))
                    .comment(formatStringByJson(String.valueOf(jsonNode.get("comment"))))
                    .dateCreated(dateCreate)
                    .isReaded(false)
                    .user(user)
                    .build();
            feedBackRepository.save(feedBack);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<?> updateFeedBack(Integer idFeedback) {
        try {
            FeedBack feedBack = feedBackRepository.findById(idFeedback).get();
            feedBack.setReaded(true);
            feedBackRepository.save(feedBack);
            return ResponseEntity.ok("Thành công");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    private String formatStringByJson(String json) {
        return json.replaceAll("\"", "");
    }
}
