package vn.ducbao.springboot.webbansach_backend.service.elk;

import lombok.Builder;
import lombok.Data;
import vn.ducbao.springboot.webbansach_backend.dto.request.SearchFilter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class SearchCriteria {
    private String indexName;
    private String keyword;
    private String[] searchFilters;
    private String sortBy;
    private int pageNo;
    private int pageSize;
    private Set<String> VALID_KEY_FIELD = new HashSet<String>();
    private List<String> VALID_FIELD_SEARCH = new ArrayList<String>();
}
