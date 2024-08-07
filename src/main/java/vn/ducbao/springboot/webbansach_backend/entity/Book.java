package vn.ducbao.springboot.webbansach_backend.entity;

import java.util.List;

import jakarta.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_book")
    private int idBook;

    @Column(name = "name_book", length = 256)
    private String nameBook;
    //    @Column(name = "cost_book")
    //    private String cost;
    @Column(name = "author", length = 512)
    private String author;

    @Column(name = "isbn", length = 256)
    private String ISBN;

    @Column(name = "list_price")
    private double listPrice;

    @Column(name = "sell_price")
    private double sellPrice;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "description")
    private String description;

    @Column(name = "avg_rating")
    private double avgRating;

    @Column(name = "sold_quantity")
    private int soldQuantity;

    @Column(name = "discount_percent")
    private double discountPercent;

    @ManyToMany(
            fetch = FetchType.EAGER,
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "book_category",
            joinColumns = @JoinColumn(name = "id_book"),
            inverseJoinColumns = @JoinColumn(name = "id_category"))
    List<Category> categoryList;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Image> imageList;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<OrderDetail> orderDetailList;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<Review> reviewList;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<FavoriteBook> favoriteBookList;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CartItem> listCartItems;
}
