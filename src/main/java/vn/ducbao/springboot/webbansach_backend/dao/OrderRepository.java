package vn.ducbao.springboot.webbansach_backend.dao;

import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import vn.ducbao.springboot.webbansach_backend.entity.Order;
import vn.ducbao.springboot.webbansach_backend.entity.User;

@RepositoryRestResource(path = "orders")
public interface OrderRepository extends JpaRepository<Order, Integer> {
    public Order findFirstByUserOrderByIdOrderDesc(User user);
}
