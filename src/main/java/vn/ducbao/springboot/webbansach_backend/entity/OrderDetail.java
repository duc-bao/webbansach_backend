package vn.ducbao.springboot.webbansach_backend.entity;

import jakarta.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name = "order_detail")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_order_detail")
    private int idOrderDetail;

    @Column(name = "is_review")
    private boolean isReview;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "id_book", nullable = false)
    private Book book;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "price")
    private double price; // Gia cua 1 cuon sach

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "id_order", nullable = false)
    private Order order;
}
