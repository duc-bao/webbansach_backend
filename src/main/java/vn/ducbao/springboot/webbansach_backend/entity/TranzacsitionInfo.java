package vn.ducbao.springboot.webbansach_backend.entity;

import java.util.Date;

import jakarta.persistence.*;

import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "tranzacsiton_info")
@Data
@Builder
public class TranzacsitionInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int amount;

    @Column(name = "date_payment")
    private Date datePayment;

    @ManyToOne(
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
            fetch = FetchType.EAGER)
    @JoinColumn(name = "id_user")
    private User user;
}
