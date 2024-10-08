package vn.ducbao.springboot.webbansach_backend.entity;

import java.sql.Date;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "feedback")
public class FeedBack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_feedback")
    private int idFeedback;

    @Column(name = "title")
    private String title;

    @Column(name = "comment")
    private String comment;

    @Column(name = "dateCreated")
    private Date dateCreated;

    @Column(name = "isReaded")
    private boolean isReaded;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "id_user", nullable = false)
    private User user;

    @Override
    public String toString() {
        return "Feedbacks{" + "idFeedback="
                + idFeedback + ", title='"
                + title + '\'' + ", comment='"
                + comment + '\'' + ", dateCreated="
                + dateCreated + ", isReaded="
                + isReaded + '}';
    }
}
