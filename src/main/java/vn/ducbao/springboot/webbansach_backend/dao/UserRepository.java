package vn.ducbao.springboot.webbansach_backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import vn.ducbao.springboot.webbansach_backend.entity.User;
@RepositoryRestResource(path = "users")
public interface UserRepository extends JpaRepository<User, Integer> {
    public boolean existsByUserName(String userName);
     public boolean existsByEmail (String email);
    public User findByUserName(String userName);
}
