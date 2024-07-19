package vn.ducbao.springboot.webbansach_backend.service.redis;

import ch.qos.logback.core.util.TimeUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class BaseRedisServiceImpl implements BaseRedisService{
    private  final   RedisTemplate<String, Object> redisTemplate;
    private final HashOperations<String, String, Object> hashOperations;

    public BaseRedisServiceImpl(RedisTemplate<String, Object> redisTemplate){
        this.redisTemplate = redisTemplate;
        hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public void set(String key, Object value) {
        log.info("Setting key: {} with value: {}", key, value);
        redisTemplate.opsForValue().set(key,value);
    }

    @Override
    public void setTimeToLive(String key, long timeoutInDays) {
        log.info("Setting TTL for key: {} with timeout (seconds): {}", key, timeoutInDays);

        redisTemplate.expire(key, timeoutInDays, TimeUnit.MICROSECONDS);
    }

    @Override
    public void hashSet(String key, String field, Object value) {
            hashOperations.put(key,field,value);
    }

    @Override
    public boolean hashExists(String key, String field) {
           return hashOperations.hasKey(key,field);
    }

    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }
    // Lấy ra 1 map
    @Override
    public Map<String, Object> getField(String key) {
        Map<String, Object> entries = hashOperations.entries(key);
        if (entries == null || entries.isEmpty()) {
            // Không có dữ liệu trong Redis, xử lý theo cách bạn muốn
            return Collections.emptyMap(); // Hoặc trả về một giá trị khác
        }
        return entries;
    }

    @Override
    public Object hashGet(String key, String field) {
        return hashOperations.get(key,field);
    }

    @Override
    public List<Object> hashGetByFieldPrefix(String key, String fieldPrefix) {
        List<Object> objects = new ArrayList<>();
        Map<String, Object> hasentries = hashOperations.entries(key);
        for (Map.Entry<String, Object> entry : hasentries.entrySet()) {
            if(entry.getKey().startsWith(fieldPrefix)) {
                objects.add(entry.getValue());
            }
        }
        return objects;
    }

    @Override
    public Set<String> getFieldPrefixes(String key) {
        return hashOperations.entries(key).keySet();
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void delete(String key, String field) {
        hashOperations.delete(key,field);

    }

    @Override
    public void delete(String key, List<String> field) {
       for (String fieldKey : field) {
           hashOperations.delete(key,fieldKey);
       }
    }
}
