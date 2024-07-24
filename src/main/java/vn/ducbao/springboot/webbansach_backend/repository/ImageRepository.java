package vn.ducbao.springboot.webbansach_backend.repository;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import vn.ducbao.springboot.webbansach_backend.entity.Book;
import vn.ducbao.springboot.webbansach_backend.entity.Image;

@RepositoryRestResource(path = "images")
public interface ImageRepository extends JpaRepository<Image, Integer> {
    public List<Image> findImagesByBook(Book book);

    @Modifying
    @Transactional
    @Query("delete from Image i where i.icon = false AND i.book.idBook = :idBook")
    public void deleteImagesWithFalseThumbnailByBookId(@Param("idBook") int idBook);
}
