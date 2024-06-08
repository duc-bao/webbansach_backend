package vn.ducbao.springboot.webbansach_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "list_favorites")
public class FavoriteBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_favorites_book")
    private int id_favorite;
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "id_user", nullable = false)
    private User user;
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "id_book", nullable = false)
    private Book book;

    @Override
    public String toString() {
        return "FavoriteBook{" +
                "id_favorite=" + id_favorite +
                ", user=" + user +
                ", book=" + book +
                '}';
    }
}

