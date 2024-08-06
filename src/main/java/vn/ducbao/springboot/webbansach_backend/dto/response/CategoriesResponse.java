package vn.ducbao.springboot.webbansach_backend.dto.response;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriesResponse {
    @Field(type = FieldType.Integer)
    private int idCategory;

    @Field(type = FieldType.Keyword)
    private String nameCategory;
}
