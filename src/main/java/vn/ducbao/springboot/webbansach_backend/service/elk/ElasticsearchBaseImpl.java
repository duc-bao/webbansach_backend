package vn.ducbao.springboot.webbansach_backend.service.elk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.JsonData;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import vn.ducbao.springboot.webbansach_backend.dto.request.SearchFilter;
import vn.ducbao.springboot.webbansach_backend.dto.response.PageResponse;
import vn.ducbao.springboot.webbansach_backend.exception.AppException;
import vn.ducbao.springboot.webbansach_backend.exception.ErrorCode;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ElasticsearchBaseImpl<T> {
    ElasticsearchClient elasticsearchClient;

    int MAX_SIZE = 10000;

    public PageResponse<T> search(SearchCriteria searchCriteria, Class<T> responseType) {
        try {
            SearchRequest searchRequest = buildSearchRequest(searchCriteria);
            SearchResponse<T> searchResponse = elasticsearchClient.search(searchRequest, responseType);
            return buildPageResponse(searchResponse, searchCriteria);
        } catch (java.io.IOException e) {
            throw new AppException(ErrorCode.INVALID_KEYORFILTERELK);
        }
    }

    private PageResponse<T> buildPageResponse(SearchResponse<T> searchResponse, SearchCriteria searchCriteria) {
        List<T> result =
                searchResponse.hits().hits().stream().map(hit -> hit.source()).collect(Collectors.toList());
        long totalHits = searchResponse.hits().total().value();
        long totalPage = (int) Math.ceil((double) totalHits / searchCriteria.getPageSize());

        return PageResponse.<T>builder()
                .page(searchCriteria.getPageNo())
                .size(searchCriteria.getPageSize())
                .total(totalHits)
                .rows(result)
                .build();
    }

    private SearchRequest buildSearchRequest(SearchCriteria searchCriteria) {
        BoolQuery.Builder boolQuery = new BoolQuery.Builder();
        if (StringUtils.hasText(searchCriteria.getKeyword())) {
            boolQuery.must(buildMultiMatchQuery(searchCriteria));
        }
        if (searchCriteria.getSearchFilters() != null && searchCriteria.getSearchFilters().length > 0) {
            List<SearchFilter> searchFilters = new ArrayList<>();
            SearchFilter filter = handleFilter(searchCriteria.getSearchFilters(), searchCriteria);
            if (filter != null) {
                searchFilters.add(filter);
            }
            for (SearchFilter searchFilter : searchFilters) {
                if (searchCriteria.getVALID_KEY_FIELD().contains(searchFilter.getKey())) {
                    addFilter(boolQuery, searchFilter);
                } else {
                    throw new AppException(ErrorCode.INVALID_KEYORFILTERELK);
                }
            }
        }
        SortOptions sortOptions = buildSortOptions(searchCriteria);

        return SearchRequest.of(s -> s.index(searchCriteria.getIndexName())
                .query(boolQuery.build()._toQuery())
                .sort(sortOptions != null ? List.of(sortOptions) : null)
                .from(searchCriteria.getPageNo() * searchCriteria.getPageSize())
                .size(Math.min(searchCriteria.getPageSize(), MAX_SIZE))
                .trackTotalHits(th -> th.enabled(true)));
    }

    private SortOptions buildSortOptions(SearchCriteria searchCriteria) {
        if (searchCriteria.getSortBy() == null || searchCriteria.getSortBy().isEmpty()) {
            return null;
        }

        String sortField = searchCriteria.getSortBy();
        boolean isDescending = sortField.startsWith("-");
        if (isDescending) {
            sortField = sortField.substring(1);
        }

        // Danh sách các trường số đã biết
        String sortFieldName;
        if (searchCriteria.getVALID_SORT_NOT_TEXT().contains(sortField)) {
            // Đối với trường số, sử dụng trực tiếp
            sortFieldName = sortField;
        } else {
            // Đối với trường văn bản, thêm .keyword
            sortFieldName = sortField + ".keyword";
        }

        return SortOptions.of(s -> s.field(f -> f
                .field(sortFieldName)
                .order(isDescending ? SortOrder.Desc : SortOrder.Asc)));
    }

    private SearchFilter handleFilter(String[] searchFilters, SearchCriteria searchCriteria) {
        for (String searchFilter : searchFilters) {
            if (StringUtils.hasText(searchFilter)) {
                Pattern pattern = Pattern.compile("(\\w+(?:\\.\\w+)*)\\(([^)]*)\\)=([^,]+)");
                Matcher matcher = pattern.matcher(searchFilter);
                if (matcher.find()) {
                    String key = matcher.group(1);
                    FilterOperator operator = converOperator(matcher.group(2));
                    String value = matcher.group(3);
                    if (searchCriteria.getVALID_KEY_FIELD().contains(key)) {
                        return new SearchFilter(key, operator, value);
                    } else {
                        throw new AppException(ErrorCode.INVALID_KEYORFILTERELK);
                    }
                } else {
                    throw new AppException(ErrorCode.INVALID_KEYORFILTERELK);
                }
            }
        }
        return null;
    }

    private FilterOperator converOperator(String operator) {
        switch (operator) {
            case ">":
                return FilterOperator.GREATER_THAN;
            case "<":
                return FilterOperator.LESS_THAN;
            case "=":
                return FilterOperator.EQUALS;
            case "IN":
                return FilterOperator.IN;
            case "LIKE":
                return FilterOperator.LIKE;
            default:
                throw new AppException(ErrorCode.INVALID_OPERATION);
        }
    }

    private void addFilter(BoolQuery.Builder boolQuery, SearchFilter searchFilter) {
        Query filterQuery = null;
        String key = searchFilter.getKey();
        switch (searchFilter.getOperation()) {
            case GREATER_THAN:
                filterQuery = RangeQuery.of(
                                r -> r.field(searchFilter.getKey()).gte(JsonData.of(searchFilter.getValue())))
                        ._toQuery();
                break;
            case LESS_THAN:
                filterQuery = RangeQuery.of(
                                r -> r.field(searchFilter.getKey()).lte(JsonData.of(searchFilter.getValue())))
                        ._toQuery();
                break;
            case EQUALS:
                if (key.contains(".")) {
                    // Tách chuỗi ra và bor vào mảng
                    String[] parts = key.split("\\.");
                    String path = parts[0];
                    // Lấy giá trị sau dấu .
                    String nestedField = String.join(".", Arrays.copyOfRange(parts, 1, parts.length));

                    filterQuery = NestedQuery.of(n -> n.path(path)
                                    .query(q -> q.bool(b -> b.filter(f -> f.term(
                                            t -> t.field(key + ".keyword").value(searchFilter.getValue()))))))
                            ._toQuery();
                } else {
                    filterQuery = MatchQuery.of(
                                    m -> m.field(searchFilter.getKey()).query(searchFilter.getValue()))
                            ._toQuery();
                }
                break;
            default:
                throw new AppException(ErrorCode.INVALID_OPERATION);
        }
        if (filterQuery != null) {
            boolQuery.filter(filterQuery); // Sử dụng filter thay vì must hoặc should tùy theo yêu cầu logic của bạn
        }
    }

    private Query buildMultiMatchQuery(SearchCriteria searchCriteria) {
        return MultiMatchQuery.of(m -> m.fields(searchCriteria.getVALID_FIELD_SEARCH())
                        .query(searchCriteria.getKeyword())
                        .operator(Operator.Or)
                        .analyzer("vi_analyzer")
                        .type(TextQueryType.BestFields))
                ._toQuery();
    }
}
