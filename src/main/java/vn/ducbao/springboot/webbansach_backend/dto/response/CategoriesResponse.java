package vn.ducbao.springboot.webbansach_backend.dto.response;

import lombok.*;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriesResponse {
    @Field(type = FieldType.Integer)
    private int idCategory;

    @Field(type = FieldType.Text)
    private String nameCategory;
}
