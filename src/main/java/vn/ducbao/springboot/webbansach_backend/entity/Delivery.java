package vn.ducbao.springboot.webbansach_backend.entity;

import java.util.List;

import jakarta.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name = "delivery")
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_delivery")
    private int idDelivery;

    @Column(name = "name_delivery")
    private String nameDelivery;

    @Column(name = "description")
    private String description;

    @Column(name = "fee_delivery")
    private double feeDelivery;

    @OneToMany(
            mappedBy = "delivery",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Order> orderList;
}
