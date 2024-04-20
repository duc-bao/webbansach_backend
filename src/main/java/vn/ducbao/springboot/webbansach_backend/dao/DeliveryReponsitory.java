package vn.ducbao.springboot.webbansach_backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import vn.ducbao.springboot.webbansach_backend.entity.Delivery;

@RepositoryRestResource(path = "deliveries")
public interface DeliveryReponsitory extends JpaRepository<Delivery, Integer> {
}
