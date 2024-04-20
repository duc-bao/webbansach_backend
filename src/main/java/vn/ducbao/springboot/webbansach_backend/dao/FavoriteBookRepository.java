package vn.ducbao.springboot.webbansach_backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import vn.ducbao.springboot.webbansach_backend.entity.FavoriteBook;

@RepositoryRestResource(path = "favorite-books")
public interface FavoriteBookRepository  extends JpaRepository<FavoriteBook, Integer> {
}
