package vn.ducbao.springboot.webbansach_backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "image")
public class Image {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id_image")
   private int idImgage;
   @Column(name = "name_image", length = 256)
   private String nameIma;
   @Column(name = "is_thumbnail")
   private boolean icon;
   @Column(name = "url_image", length = 512)
   private String linkImg;
   @Column(name = "data_image", length = 256)
   @Lob
   private String dataImg;
   @ManyToOne(
           cascade = {
                   CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH
           }
   )
   @JoinColumn(name = "id_book", nullable = false)
   private Book book;
}
