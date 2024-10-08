package vn.ducbao.springboot.webbansach_backend.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.*;

import org.springframework.data.annotation.CreatedDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private int id;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    @Column(name = "date_create")
    private Date dateCreate = new Date();
}
