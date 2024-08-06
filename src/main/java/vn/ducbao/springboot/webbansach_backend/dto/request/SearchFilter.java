package vn.ducbao.springboot.webbansach_backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import vn.ducbao.springboot.webbansach_backend.service.elk.FilterOperator;

@Getter
@AllArgsConstructor
@Builder
public class SearchFilter {
    private String key;
    private FilterOperator operation;
    private String value;
}
