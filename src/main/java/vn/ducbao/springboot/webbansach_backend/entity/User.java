package vn.ducbao.springboot.webbansach_backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.util.List;

@Data
@Entity
@Table(name = "user")
public class User extends  AbstractEntity  {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id_user")
//    private int idUser;
    @Column(name = "first_name", length = 256)
    private String firstName;
    @Column(name = "last_name", length = 256)
    private String lastName;
    @Column(name = "username", length = 256)
    private String username;
    @Column(name = "password", length = 512)
    private String password;
    @Column(name = "date_of_birth")
    private Date dateOfBirth;
    @Column(name = "gender")
    private String gender;
    @Column(name = "email")
    private String email;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "purchase_address")
    private String purchaseAdrress;
    @Column(name = "delivery_address")
    private String deliveryAdress;
    @Column(name = "avatar")
    private String avatar; // Ảnh đại diện
    @Column(name = "enabled")
    private boolean enabled; // Trạng thái kích hoạt
    @Column(name = "activation_code")
    private String activationCode; // Mã kích hoạt

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    List<Review> reviewList;
    @ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "id_user"),
            inverseJoinColumns = @JoinColumn(name = "id_role"))
    List<Role> roleList;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    List<Order> orderList;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    List<FavoriteBook> favoriteBookList;
    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CartItem> listCartItems;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<FeedBack> listFeedbacks;
}
