package vn.ducbao.springboot.webbansach_backend.repository.elk;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import vn.ducbao.springboot.webbansach_backend.dto.response.BookListResponse;

@Repository
public interface BookElkRepository extends ElasticsearchRepository<BookListResponse, Integer> {}
