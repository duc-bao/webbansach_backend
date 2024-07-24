package vn.ducbao.springboot.webbansach_backend.entity;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cart_item")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cart")
    private int idCart;

    @Column(name = "quantity")
    private int quantity;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "id_book", nullable = false)
    private Book book;

    @ManyToOne()
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    public CartItem(int quantity, Book book, User user) {
        this.quantity = quantity;
        this.book = book;
        this.user = user;
    }

    @Override
    public String toString() {
        return "CartItem{" + "idCart=" + idCart + ", quantity=" + quantity + ", book=" + book + ", user=" + user + '}';
    }
}
