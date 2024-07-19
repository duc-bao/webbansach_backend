package vn.ducbao.springboot.webbansach_backend.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import vn.ducbao.springboot.webbansach_backend.entity.CartItem;

import java.util.List;

@RepositoryRestResource(path = "cart-items")
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    @Modifying
    @Transactional
    @Query("delete  from CartItem c where c.user.idUser = :idUser ")
    public  void deleteCartItemByIdUser(@Param("idUser") int idUser);
    @Modifying
    @Transactional
    @Query("SELECT ci FROM CartItem ci WHERE ci.user.idUser = :userId")
    List<CartItem> findByUserId(@Param("userId") int userId);
}
