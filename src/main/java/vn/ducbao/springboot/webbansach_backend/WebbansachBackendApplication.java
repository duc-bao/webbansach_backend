package vn.ducbao.springboot.webbansach_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// @EnableJpaRepositories(
//        includeFilters = @ComponentScan.Filter(
//                type = FilterType.ASSIGNABLE_TYPE, classes = JpaRepository.class))
//
// @EnableElasticsearchRepositories(basePackages = "vn.ducbao.springboot.webbansach_backend.repository.elk")

public class WebbansachBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebbansachBackendApplication.class, args);
    }
}
