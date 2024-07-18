package vn.ducbao.springboot.webbansach_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import vn.ducbao.springboot.webbansach_backend.entity.OrderDetail;
import vn.ducbao.springboot.webbansach_backend.entity.Review;
@RepositoryRestResource(path = "reviews")
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    public  Review findReviewByOrderDetail(OrderDetail orderDetail);
    public  long countBy();
}
