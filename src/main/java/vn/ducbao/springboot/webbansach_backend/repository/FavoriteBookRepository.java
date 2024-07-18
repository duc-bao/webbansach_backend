package vn.ducbao.springboot.webbansach_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import vn.ducbao.springboot.webbansach_backend.entity.Book;
import vn.ducbao.springboot.webbansach_backend.entity.FavoriteBook;
import vn.ducbao.springboot.webbansach_backend.entity.User;

import java.util.List;

@RepositoryRestResource(path = "favorite-books")
public interface FavoriteBookRepository  extends JpaRepository<FavoriteBook, Integer> {
    public List<FavoriteBook> findFavoriteBookByUser(User user);
    public FavoriteBook findFavoriteBookByBookAndUser(Book book, User user);
}
