package vn.ducbao.springboot.webbansach_backend.dto.response;

import java.util.List;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(indexName = "book")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookListResponse {
    @org.springframework.data.annotation.Id
    @Field(type = FieldType.Integer)
    private int idBook;

    @Field(type = FieldType.Text, analyzer = "vietnamese_no_tone", searchAnalyzer = "vietnamese_no_tone")
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
    private int quantity;

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
