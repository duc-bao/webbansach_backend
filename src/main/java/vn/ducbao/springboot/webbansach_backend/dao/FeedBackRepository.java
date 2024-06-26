package vn.ducbao.springboot.webbansach_backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import vn.ducbao.springboot.webbansach_backend.entity.FeedBack;
@RepositoryRestResource(path = "feedbacks")
public interface FeedBackRepository extends JpaRepository<FeedBack, Integer> {
}
