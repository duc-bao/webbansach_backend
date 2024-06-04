package vn.ducbao.springboot.webbansach_backend.service.book;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.ducbao.springboot.webbansach_backend.dao.BookRepository;
import vn.ducbao.springboot.webbansach_backend.dao.CategoryRepository;
import vn.ducbao.springboot.webbansach_backend.dao.ImageRepository;
import vn.ducbao.springboot.webbansach_backend.entity.Book;
import vn.ducbao.springboot.webbansach_backend.entity.Category;
import vn.ducbao.springboot.webbansach_backend.entity.Image;
import vn.ducbao.springboot.webbansach_backend.service.image.ImageService;
import vn.ducbao.springboot.webbansach_backend.service.util.Base64MuiltipartFileConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService{

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private ImageService imageService;
    public  final ObjectMapper objectMapper;

    public BookServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public ResponseEntity<?> save(JsonNode jsonNode) throws JsonProcessingException {
        try {
            Book book = objectMapper.treeToValue(jsonNode, Book.class);
            System.out.println(book);
            // Lưu thể loại sách
            List<Integer> idcategoryList = objectMapper.readValue(jsonNode.get("idGenres").traverse(), new TypeReference<List<Integer>>() {
            });
            List<Category> categoryList = new ArrayList<>();
            for(int idCat : idcategoryList){
                Optional<Category> category = categoryRepository.findById(idCat);
                categoryList.add(category.get());
            }
            book.setCategoryList(categoryList);
            // Lưu trước để lấy id sách đặt tên cho ảnh
            Book newBook =  bookRepository.save(book);
            // neeus cos ma giam gia
            if(book.getDiscountPercent() != 0){
                int sellPrice = (int) ((int) book.getListPrice() - Math.round(book.getListPrice())/book.getDiscountPercent());
                book.setSellPrice(sellPrice);
            }
            // Lưu thumbnail cho ảnh
            String datathumbnail = formatStringByJson(String.valueOf((jsonNode.get("thumbnail"))));

            // Lưu ảnh
            Image thumbnail = new Image();
            thumbnail.setBook(newBook);
            //thumbnail.setDataImg(formatStringByJson(String.valueOf((jsonNode.get("thumbnail")))));
            thumbnail.setIcon(true);
            MultipartFile multipartFile = Base64MuiltipartFileConverter.convert(datathumbnail);
            String thumbnailUrl = imageService.uploadImage(multipartFile, "Book_" + newBook.getIdBook());
            thumbnail.setLinkImg(thumbnailUrl);
            List<Image> imageList = new ArrayList<>();
            imageList.add(thumbnail);
            // Lưu ảnh có liên quan
            String dataReleatedImg = formatStringByJson(String.valueOf((jsonNode.get("relatedImg"))));
            List<String> arrDataReleatedImg = objectMapper.readValue(jsonNode.get("relatedImg").traverse(), new TypeReference<List<String>>() {
            });
            for(int  i  = 0; i < arrDataReleatedImg.size(); i++){
                String img = arrDataReleatedImg.get(i);
                Image image = new Image();
                image.setBook(newBook);
                image.setIcon(false);
                MultipartFile relatedImageFile = Base64MuiltipartFileConverter.convert(img);
                String imgUrl = imageService.uploadImage(relatedImageFile, "Book_" + newBook.getIdBook()+"." + i);
                image.setLinkImg(imgUrl);
                imageList.add(image);
            }
            newBook.setImageList(imageList);
            //Cap nhat lai anh
            bookRepository.save(newBook);
            return ResponseEntity.ok("Success!");
        }catch (Exception e){
            return  ResponseEntity.badRequest().build();
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> update(JsonNode jsonNode) throws JsonProcessingException {
        try {
            Book book = objectMapper.treeToValue(jsonNode, Book.class);
            List<Image> imageList = imageRepository.findImagesByBook(book);
            // Luu the loai sach
            List<Integer> idCategories = objectMapper.readValue(jsonNode.get("idGenres").traverse(), new TypeReference<List<Integer>>() {
            });
            List<Category> categoryList   =new ArrayList<>();
            for(int idCat : idCategories){
                Optional<Category> category = categoryRepository.findById(idCat);
                categoryList.add(category.get());
            }
            book.setCategoryList(categoryList);
            // Kieem tra xem thumbnail co thay doi khong
            String datathumbnail = formatStringByJson(String.valueOf(jsonNode.get("thumbnail")));
            if(Base64MuiltipartFileConverter.isBase64(datathumbnail)){
                for (Image image : imageList){
                    if(image.isIcon()){
                        MultipartFile multipartFile = Base64MuiltipartFileConverter.convert(datathumbnail);
                        String thumbnailUrl = imageService.uploadImage(multipartFile, "Book_" + book.getIdBook());
                        image.setLinkImg(thumbnailUrl);
                        imageRepository.save(image);
                        break;
                    }
                }
            }
            Book newbook = bookRepository.save(book);
            // Kieemr  tra anhr co lien quan
            List<String> arrDataImage = objectMapper.readValue(jsonNode.get("relateImg").traverse(), new TypeReference<List<String>>() {
            });
            // Xem có xoá tất ở bên FE không
            boolean isCheckDelete  = true;
            for (String img : arrDataImage){
                if(!Base64MuiltipartFileConverter.isBase64(img)){
                    isCheckDelete = false;
                }
            }
            // Neu xoa het tat ca
            if(isCheckDelete){
                imageRepository.deleteImagesWithFalseThumbnailByBookId(newbook.getIdBook());
                Image thubnailTemp = imageList.get(0);
                imageList.clear();
                imageList.add(thubnailTemp);
                for(int i = 0; i < arrDataImage.size(); i++){
                    String img = arrDataImage.get(i);
                    Image image = new Image();
                    image.setBook(newbook);
                    image.setIcon(false);
                    MultipartFile multipartFile = Base64MuiltipartFileConverter.convert(img);
                    String imgUrl = imageService.uploadImage(multipartFile, "Book_" + newbook.getIdBook() + "." + i);
                    image.setLinkImg(imgUrl);
                    imageList.add(image);
                }
            }else {
                // Neu khong xoa het tat ca va giu nguyen anh
                for(int i = 0 ; i < arrDataImage.size(); i++){
                    String img = arrDataImage.get(i);
                    if(Base64MuiltipartFileConverter.isBase64(img)){
                        Image image = new Image();
                        image.setBook(newbook);
                        image.setIcon(false);
                        MultipartFile multipartFile = Base64MuiltipartFileConverter.convert(img);
                        String imgUrl = imageService.uploadImage(multipartFile, "Book_" + newbook.getIdBook()+ "." + i);
                        image.setLinkImg(imgUrl);
                        imageRepository.save(image);
                    }
                }
            }
            newbook.setImageList(imageList);
            bookRepository.save(newbook);
            return  ResponseEntity.ok("SUCCESS!");
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    public String formatStringByJson(String json){
        return  json.replace("\"", "");
    }
}
