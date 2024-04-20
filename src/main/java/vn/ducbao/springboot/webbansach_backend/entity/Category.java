package vn.ducbao.springboot.webbansach_backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
@Data
@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_category")
    private int idCategory;
    @Column(name = "name_category")
    private String nameCategory;
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},fetch = FetchType.LAZY)
    @JoinTable(name = "book_category",
       joinColumns = @JoinColumn(name = "id_category"),
       inverseJoinColumns = @JoinColumn(name = "id_book")
    )
    private List<Book> bookList;
}
