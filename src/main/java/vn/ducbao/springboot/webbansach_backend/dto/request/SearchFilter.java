package vn.ducbao.springboot.webbansach_backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class SearchFilter {
    private String key;
    private String operation;
    private String value;
}
