package vn.ducbao.springboot.webbansach_backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_payment")
    private  int idPayment;
    @Column(name = "name_payment")
    private String namePayment;
    @Column(name = "description")
    private String description;
    @Column(name = "fee_payment")
    private double feePayment;
    @OneToMany(mappedBy = "payment", fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    List<Order> orderList;
}
