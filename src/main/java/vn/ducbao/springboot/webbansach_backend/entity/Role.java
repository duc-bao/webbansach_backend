package vn.ducbao.springboot.webbansach_backend.entity;

import java.util.List;

import jakarta.persistence.*;

import org.springframework.security.core.GrantedAuthority;

import lombok.Data;

@Data
@Entity
@Table(name = "role")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_role")
    private int idRole;

    @Column(name = "name_role")
    private String nameRole;

    @ManyToMany(
            cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
            fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "id_role"),
            inverseJoinColumns = @JoinColumn(name = "id_user"))
    List<User> userList;

    @Override
    public String getAuthority() {
        return nameRole;
    }
}
