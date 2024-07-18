//package vn.ducbao.springboot.webbansach_backend.service;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.AccessLevel;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Service;
//import vn.ducbao.springboot.webbansach_backend.dto.response.BookListResponse;
//import vn.ducbao.springboot.webbansach_backend.entity.Book;
//import vn.ducbao.springboot.webbansach_backend.service.redis.BaseRedisService;
//
//import java.util.List;
//@Service
//@RequiredArgsConstructor
//@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
//public class BookRedisService {
//    RedisTemplate<String, Object> redisTemplate;
//    BaseRedisService baseRedisService;
//    ObjectMapper   objectMapper;
//    public List<Book> getAllProduct(String keyword, int categoryId, PageRequest pageRequest) throws JsonProcessingException {
//        String key = keyProductRedis(pageRequest);
//        String json = (String) baseRedisService.get(key);
//        List<Book> bookList = json != null ? objectMapper.readValue(json, new TypeReference<List<Book>>() {}) : null;
//        return bookList;
//    }
//    public void clear(){
//        redisTemplate.getConnectionFactory().getConnection().flushAll();
//    }
//    public void save(List<Book> bookListResponses, String keyword, int categoryId, PageRequest pageRequest) throws JsonProcessingException {
//        String key = keyProductRedis(pageRequest);
//        String json = objectMapper.writeValueAsString(bookListResponses);
//        baseRedisService.set(key, json);
//        baseRedisService.setTimeToLive(key, 100000);
//    }
//    private String keyProductRedis(PageRequest pageRequest) {
//        int pageNumber = pageRequest.getPageNumber();
//        int pageSize = pageRequest.getPageSize();
//        Sort sort = pageRequest.getSort();
//        String sortDirection = sort.getOrderFor("id").getDirection() == Sort.Direction.ASC ? "asc" : "desc";
//        String key = String.format("all_product:%s-%s-%s", pageNumber, pageSize, sortDirection);
//        return  key;
//    }
//}
