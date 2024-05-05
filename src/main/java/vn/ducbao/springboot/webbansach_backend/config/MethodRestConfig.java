package vn.ducbao.springboot.webbansach_backend.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import vn.ducbao.springboot.webbansach_backend.entity.Category;
import vn.ducbao.springboot.webbansach_backend.entity.User;

@Configuration
public class MethodRestConfig implements RepositoryRestConfigurer {
    private String url = "http://localhost:3000";
    @Autowired
    private EntityManager entityManager;
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        // Cấu hình thêm là cho phép trả về id của entity khi gọi endpoint get
        config.exposeIdsFor(entityManager.getMetamodel().getEntities().stream().map(Type::getJavaType).toArray(Class[]::new));

        //CORS Configuration
        cors.addMapping("/**").allowedOrigins(url).allowedMethods("GET", "PUT", "POST", "DELETE");

        //Chặn các phương thức nào khác phương thức get chặn tất cả
        HttpMethod[] disableMethod = {
                HttpMethod.POST,
                HttpMethod.PUT,
                HttpMethod.DELETE,
                HttpMethod.PATCH
        };

//        //Chặn các phương thức của class entity cụ thể
//        disableHttpMethod(Category.class, config, disableMethod);
//        //Chặn phương thức của class User
//        HttpMethod[] disableMethodUser = {
//                HttpMethod.DELETE
//        };
//        disableHttpMethod(User.class, config, disableMethodUser);
    }
    // CHặn riêng lẻ từng class một với các phương thức http riêng lẻ
    private  void disableHttpMethod(Class c, RepositoryRestConfiguration configurer,HttpMethod[] httpMethods){
        configurer.getExposureConfiguration().forDomainType(c)
                .withItemExposure(((metdata, httpMethods1) -> httpMethods1.disable(httpMethods)))
                .withCollectionExposure((metdata, httpMethods1) -> httpMethods1.disable(httpMethods));
    }

}
