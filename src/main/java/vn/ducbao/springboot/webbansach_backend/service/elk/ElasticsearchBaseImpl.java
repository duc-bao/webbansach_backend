package vn.ducbao.springboot.webbansach_backend.service.elk;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.JsonData;
import io.jsonwebtoken.io.IOException;
import io.netty.util.internal.StringUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import vn.ducbao.springboot.webbansach_backend.dto.request.SearchFilter;
import vn.ducbao.springboot.webbansach_backend.dto.response.PageResponse;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ElasticsearchBaseImpl<T> {
    ElasticsearchClient elasticsearchClient;

    int MAX_SIZE = 10000;
    public PageResponse<T> search(SearchCriteria searchCriteria, Class<T> responseType){
        try {
            SearchRequest  searchRequest = buildSearchRequest(searchCriteria);
            SearchResponse<T> searchResponse = elasticsearchClient.search(searchRequest, responseType);
            return buildPageResponse(searchResponse, searchCriteria);
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }

    private PageResponse<T> buildPageResponse(SearchResponse<T> searchResponse, SearchCriteria searchCriteria) {
            List<T> result = searchResponse.hits().hits().stream().map(
                    hit -> hit.source()).collect(Collectors.toList());
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
        if(StringUtils.hasText(searchCriteria.getKeyword())){
            boolQuery.must(buildMultiMatchQuery(searchCriteria));
        }
        if(searchCriteria.getSearchFilters() != null && !searchCriteria.getSearchFilters().isEmpty()){
            for(SearchFilter searchFilter : searchCriteria.getSearchFilters()){
                if(searchCriteria.getVALID_KEY_FIELD().contains(searchFilter.getKey())){
                    addFilter(boolQuery, searchFilter);
                }else {
                    throw new IllegalArgumentException("Invalid search filter: " + searchFilter.getKey());
                }
            }

        }
        SortOptions sortOptions;
        if(searchCriteria.getSortBy() != null && !searchCriteria.getSortBy().isEmpty()){
            if(searchCriteria.getSortBy().startsWith("-")){
                String field = searchCriteria.getSortBy().substring(1); // Remove the "-" for the field name
                 sortOptions = SortOptions.of(s -> s.field(f -> f.field(field).order(SortOrder.Desc)));
            }else {
                 sortOptions = SortOptions.of(s-> s.field(f -> f.field(searchCriteria.getSortBy()).order(SortOrder.Asc)));
            }
        } else {
            sortOptions = null;
        }
        return  SearchRequest.of(s -> s.index(searchCriteria.getIndexName())
                .query(boolQuery.build()._toQuery())
                .sort(sortOptions != null ? List.of(sortOptions) : null)
                .from(searchCriteria.getPageNo()*searchCriteria.getPageSize())
                .size(Math.min(searchCriteria.getPageSize(), MAX_SIZE))
                .trackTotalHits(th ->th.enabled(true))
        );
    }

    private void addFilter(BoolQuery.Builder boolQuery, SearchFilter searchFilter) {
        Query filterQuery = null;
        switch (searchFilter.getOperation()){
            case GREATER_THAN: RangeQuery.of(r ->r.field(searchFilter.getKey()).gte(JsonData.of(searchFilter.getValue())));
            break;
            case LESS_THAN: RangeQuery.of(r ->r.field(searchFilter.getKey()).lte(JsonData.of(searchFilter.getValue())));
            break;
            case EQUALS: TermQuery.of(td -> td.field(searchFilter.getKey()).value(searchFilter.getValue()));
            break;
            default:
                throw new IllegalArgumentException("Invalid operation: " + searchFilter.getOperation());
        }

    }

    private Query buildMultiMatchQuery(SearchCriteria searchCriteria) {
            return MultiMatchQuery.of(m -> m.fields(searchCriteria.getSearchFields())
                    .query(searchCriteria.getKeyword())
                    .fuzziness("AUTO")
                    .operator(Operator.Or)
            )._toQuery();
    }

}
