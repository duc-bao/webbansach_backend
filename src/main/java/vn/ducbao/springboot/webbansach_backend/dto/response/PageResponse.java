package vn.ducbao.springboot.webbansach_backend.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PageResponse<T> {
    private long total;
    private List<T> rows;
    private int size;
    private int page;
}
