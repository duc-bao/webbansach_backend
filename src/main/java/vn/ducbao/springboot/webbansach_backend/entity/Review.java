package vn.ducbao.springboot.webbansach_backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;

@Data
@Entity
@Table(name = "review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_review")
    private  int idReview;
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.MERGE,CascadeType.REFRESH})
    @JoinColumn(name = "id_user", nullable = false)
    private User user;
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.MERGE,CascadeType.REFRESH})
    @JoinColumn(name = "id_book", nullable = false)
    private Book book;
    @Column(name = "comment")
    private String comment;
    @Column(name = "rating_points")
    private  double ratingPoints;
    @Column(name = "date_created")
    private Date dateCreated;
}
