package vn.ducbao.springboot.webbansach_backend.service.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface BaseRedisService {
    // Lưu key value vao trong redis
    void set(String key, Object value);
    // Xet du lieu thoi gian ton tai
    void setTimeToLive(String key, long timeInMiliisecond);

    void hashSet(String key, String field, Object value);
    // kiem tra xem co ton tai ko
    boolean hashExists(String key, String field);
    // Lay gia tri
    Object get(String key);

    public Map<String, Object> getField(String key);

    Object hashGet(String key, String field);

    List<Object> hashGetByFieldPrefix(String key, String fieldPrefix);

    Set<String> getFieldPrefixes(String key);

    void delete(String key);

    void delete(String key, String field);

    void delete(String key, List<String> field);
}
