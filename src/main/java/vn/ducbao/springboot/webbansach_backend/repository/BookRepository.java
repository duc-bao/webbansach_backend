package vn.ducbao.springboot.webbansach_backend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.RequestParam;

import vn.ducbao.springboot.webbansach_backend.entity.Book;

@RepositoryRestResource(path = "books")
public interface BookRepository extends JpaRepository<Book, Integer> {
    Page<Book> findByNameBookContaining(@RequestParam("name_book") String nameBook, Pageable pageable);

    //    Lấy danh sách thể loại
    Page<Book> findByCategoryList_IdCategory(@RequestParam("id_category") int idCategory, Pageable pageable);
    // Lấy sách và thể loại
    Page<Book> findByNameBookContainingAndCategoryList_IdCategory(
            @RequestParam("name_book") String nameBook, @RequestParam("id_category") int idCategory, Pageable pageable);

    Page<Book> findAllByIsDeletedFalse(Pageable pageable);
    long count();
}
