package vn.ducbao.springboot.webbansach_backend.entity;

import java.sql.Date;
import java.util.List;

import jakarta.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_order")
    private int idOrder;

    @Column(name = "date_created")
    private Date dateOrder;

    @Column(name = "purchase_address")
    private String purchaseAddress;

    @Column(name = "delivery_address")
    private String deliveryAddress;

    @Column(name = "total_price_product")
    private double totalPriceProduct;

    @Column(name = "fee_payment")
    private double feePayment;

    @Column(name = "status")
    private String status;

    @Column(name = "note")
    private String note;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "fee_delivery")
    private double feeDelivery;

    @Column(name = "total_price")
    private double totalPrice;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "id_user")
    private User user;

    @Column(name = "status_payment")
    private String statusPayment;

    @Column(name = "status_delivery")
    private String statusDelivery;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderDetail> orderDetailList;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "id_payment", nullable = false)
    private Payment payment;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "id_delivery", nullable = false)
    private Delivery delivery;
}
