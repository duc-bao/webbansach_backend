package vn.ducbao.springboot.webbansach_backend.service.elk;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchCriteria {
    private String indexName;
    private String keyword;
    private String[] searchFilters;
    private String sortBy;
    private int pageNo;
    private int pageSize;
    private Set<String> VALID_KEY_FIELD = new HashSet<String>();
    private List<String> VALID_FIELD_SEARCH = new ArrayList<String>();
    private List<String> VALID_SORT_NOT_TEXT = new ArrayList<>();
    public String getKeyword() {
        return keyword != null ? keyword : "";
    }

    public String[] getSearchFilters() {
        return searchFilters != null ? searchFilters : new String[0];
    }
}
