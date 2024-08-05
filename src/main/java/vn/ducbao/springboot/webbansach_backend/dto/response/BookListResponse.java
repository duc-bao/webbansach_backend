package vn.ducbao.springboot.webbansach_backend.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import vn.ducbao.springboot.webbansach_backend.entity.Category;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(indexName = "book")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookListResponse {
    @org.springframework.data.annotation.Id
    @Field(type = FieldType.Integer)
    private int  idBook;

    @Field(type = FieldType.Text)
    private String nameBook;

    @Field(type = FieldType.Text)
    private String author;

    @Field(type = FieldType.Text)
    private String ISBN;

    @Field(type = FieldType.Double)
    private double listPrice;

    @Field(type = FieldType.Double)
    private double sellPrice;

    @Field(type = FieldType.Integer)
    int quantity;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Double)
    private double avgRating;

    @Field(type = FieldType.Integer)
    private int soldQuantity;

    @Field(type = FieldType.Double)
    private double discountPercent;

    @Field(type = FieldType.Nested, includeInParent = true)
    List<CategoriesResponse> categoryList;
}
