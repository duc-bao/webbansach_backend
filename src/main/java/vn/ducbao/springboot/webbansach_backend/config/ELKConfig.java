package vn.ducbao.springboot.webbansach_backend.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;

@Configuration
@EnableElasticsearchRepositories

////@EnableElasticsearchRepositories(basePackages = "vn.ducbao.springboot.webbansach_backend.repository.elk")
//@ComponentScan(basePackages = {"vn.ducbao.springboot.webbansach_backend.service"})
public class ELKConfig {
    @Bean
    public RestClient getRestClient() {
        RestClient restClient =
                RestClient.builder(new HttpHost("localhost", 9200)).build();
        return restClient;
    }

    @Bean

    public ElasticsearchTransport getElasticsearchTransport() {
        return new RestClientTransport(getRestClient(), new JacksonJsonpMapper());
    }

    @Bean

    public ElasticsearchClient getElasticsearchClient() {
        ElasticsearchClient client = new ElasticsearchClient(getElasticsearchTransport());
        return client;
    }

//
//    @Bean
//    public ModelMapper modelMapper() {
//        // Tạo object và cấu hình
//        ModelMapper modelMapper = new ModelMapper();
//        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
//        return modelMapper;
//    }
}
