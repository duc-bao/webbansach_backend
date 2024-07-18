package vn.ducbao.springboot.webbansach_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import vn.ducbao.springboot.webbansach_backend.entity.Order;
import vn.ducbao.springboot.webbansach_backend.entity.OrderDetail;

import java.util.List;

@RepositoryRestResource(path = "order-details")
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    public List<OrderDetail> findOrderDetailsByOrder(Order order);
}
