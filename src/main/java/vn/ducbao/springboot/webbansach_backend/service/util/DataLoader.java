//package vn.ducbao.springboot.webbansach_backend.service.util;
//
//import com.github.javafaker.Faker;
//import jakarta.transaction.Transactional;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//import vn.ducbao.springboot.webbansach_backend.entity.Book;
//import vn.ducbao.springboot.webbansach_backend.entity.Category;
//import vn.ducbao.springboot.webbansach_backend.repository.BookRepository;
//import vn.ducbao.springboot.webbansach_backend.repository.CategoryRepository;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
//@Component
//public class DataLoader implements CommandLineRunner {
//    @Autowired
//    private BookRepository bookRepository;
//
//    @Autowired
//    private CategoryRepository categoryRepository;
//    @Transactional
//    @Override
//    public void run(String... args) throws Exception {
//        Faker faker = new Faker();
//        Random random = new Random();
//        List<Category> categoryList = new ArrayList<>();
//        for(int i = 0; i < 10; i++){
//            Category category = new Category();
//            category.setNameCategory(faker.book().genre());
//            categoryList.add(category);
//        }
//        categoryRepository.saveAll(categoryList);
//        List<Category> persistedCategories = categoryRepository.findAll();
//        for (int i = 0; i < 9000; i++) {
//            Book book = new Book();
//            book.setNameBook(faker.book().title());
//            book.setAuthor(faker.book().author());
//            book.setISBN(faker.code().isbn13());
//            book.setListPrice(faker.number().randomDouble(2, 10, 100));
//            book.setSellPrice(faker.number().randomDouble(2, 5, 90));
//            book.setQuantity(faker.number().numberBetween(0, 1000));
//            book.setDescription(faker.lorem().paragraph(2));
//            book.setAvgRating(faker.number().randomDouble(2, 1, 5));
//            book.setSoldQuantity(faker.number().numberBetween(0, 5000));
//            book.setDiscountPercent(faker.number().randomDouble(2,1,50));
//            // Assign random categories
//            List<Category> bookCategories = new ArrayList<>();
//            int categoryCount = random.nextInt(3) + 1; // 1 to 3 categories per book
//            for (int j = 0; j < categoryCount; j++) {
//                bookCategories.add(persistedCategories.get(random.nextInt(persistedCategories.size())));
//            }
//            book.setCategoryList(bookCategories);
//
//            bookRepository.saveAndFlush(book);
//        }
//    }
//}
