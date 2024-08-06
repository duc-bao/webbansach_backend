package vn.ducbao.springboot.webbansach_backend.service.book;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.JsonData;
import lombok.extern.slf4j.Slf4j;
import vn.ducbao.springboot.webbansach_backend.dto.request.SearchFilter;
import vn.ducbao.springboot.webbansach_backend.dto.response.BookListResponse;
import vn.ducbao.springboot.webbansach_backend.dto.response.CategoriesResponse;
import vn.ducbao.springboot.webbansach_backend.dto.response.PageResponse;
import vn.ducbao.springboot.webbansach_backend.entity.Book;
import vn.ducbao.springboot.webbansach_backend.entity.Category;
import vn.ducbao.springboot.webbansach_backend.entity.Image;
import vn.ducbao.springboot.webbansach_backend.repository.BookRepository;
import vn.ducbao.springboot.webbansach_backend.repository.CategoryRepository;
import vn.ducbao.springboot.webbansach_backend.repository.ImageRepository;
import vn.ducbao.springboot.webbansach_backend.repository.elk.BookElkRepository;
import vn.ducbao.springboot.webbansach_backend.service.image.ImageService;
import vn.ducbao.springboot.webbansach_backend.service.util.Base64MuiltipartFileConverter;

@Service
@Slf4j
public class BookServiceImpl implements BookService {

    private static final Logger log = LoggerFactory.getLogger(BookServiceImpl.class);

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private BookElkRepository bookElkRepository;

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    private static final Set<String> VALID_KEYS =
            Set.of("nameBook", "author", "sellPrice", "categoryList.nameCategory");

    public final ObjectMapper objectMapper;
    public final ModelMapper modelMapper;

    public BookServiceImpl(ObjectMapper objectMapper, ModelMapper modelMapper) {
        this.objectMapper = objectMapper;
        this.modelMapper = modelMapper;
    }

    @Override
    public PageResponse<?> searchElk(int pageNo, int pageSize, String sortBy, String[] filter, String keyword) {
        BoolQuery.Builder builder = new BoolQuery.Builder();
        List<SearchFilter> searchFilters = new ArrayList<>();

        if (keyword != null) {
            Query multiMatchQuery = MultiMatchQuery.of(
                            m -> m.fields(Arrays.asList("nameBook", "author", "categoryList.nameCategory"))
                                    .query(keyword))
                    ._toQuery();
            builder.must(multiMatchQuery);
        }
        // Sử lí map các filter
        if (filter != null) {
            for (String filterStr : filter) {
                // Regex :(\w+)\((\w+)\)=(.*)
                Pattern pattern = Pattern.compile("(\\w+(?:\\.\\w+)*)\\(([^)]*)\\)=([^,]+)");
                Matcher matcher = pattern.matcher(filterStr);
                if (matcher.find()) {
                    String key = matcher.group(1);
                    String operator =
                            matcher.group(2) != null ? matcher.group(2) : ":"; // default to "equals" if no operator
                    String value = matcher.group(3);

                    if (VALID_KEYS.contains(key)) {
                        searchFilters.add(new SearchFilter(key, operator, value));
                    } else {
                        throw new IllegalArgumentException("Invalid Key: " + key);
                    }
                } else {
                    throw new IllegalArgumentException("Invalid filter format: " + filterStr);
                }
            }
            for (SearchFilter searchFilter : searchFilters) {
                applyFilter(builder, searchFilter);
            }
        }
        SortOptions sortOptions = getSortOptions(sortBy);
        SearchRequest request = SearchRequest.of(s -> s.index("book")
                .query(builder.build()._toQuery())
                .sort(sortOptions != null ? List.of(sortOptions) : null)
                .from(pageNo * pageSize)
                .size(pageSize));
        log.debug("Truy vấn chuối: " + request.toString());
        SearchResponse<BookListResponse> searchResponse;
        try {
            searchResponse = elasticsearchClient.search(request, BookListResponse.class);
        } catch (IOException e) {
            throw new RuntimeException("Error executing search request", e);
        }
        List<BookListResponse> result =
                searchResponse.hits().hits().stream().map(hit -> hit.source()).collect(Collectors.toList());
        return PageResponse.<BookListResponse>builder()
                .page(pageNo)
                .size(pageSize)
                .rows(result)
                .total(searchResponse.hits().total().value())
                .build();
    }

    private SortOptions getSortOptions(String sortBy) {
        if (sortBy != null) {
            if (sortBy.startsWith("-")) {
                String field = sortBy.substring(1); // Remove the "-" for the field name
                return SortOptions.of(s -> s.field(f -> f.field(field).order(SortOrder.Desc)));
            } else {
                return SortOptions.of(s -> s.field(f -> f.field(sortBy).order(SortOrder.Asc)));
            }
        }
        return null;
    }

    private void applyFilter(BoolQuery.Builder boolQuery, SearchFilter searchFilter) {
        Query filterQuery = null;
        String field = searchFilter.getKey();
        String operation = searchFilter.getOperation();
        String value = searchFilter.getValue();
        switch (operation) {
            case "gte":
                filterQuery = RangeQuery.of(r -> r.field(field).gte(JsonData.of(value)))
                        ._toQuery();
                break;
            case "lte":
                filterQuery = RangeQuery.of(r -> r.field(field).lte(JsonData.of(value)))
                        ._toQuery();
                break;
            case ":":
                if ("categoryList.nameCategory".equals(searchFilter.getKey())) {
                    filterQuery = MatchPhraseQuery.of(
                                    m -> m.field("categoryList.nameCategory").query(searchFilter.getValue()))
                            ._toQuery();
                } else {
                    throw new IllegalArgumentException("Invalid Key: " + searchFilter.getKey());
                }
                break;
            default:
                throw new IllegalArgumentException("Unsupported operation: " + searchFilter.getOperation());
        }
        boolQuery.filter(filterQuery);
    }

    //    @Override
    //    public List<Book> getBook(String keyword, int categoryId, int page, int size) throws JsonProcessingException {
    //        int totalPage = 0;
    //        PageRequest pageRequest =   PageRequest.of(page, size,
    //                Sort.by("id").descending());
    //        List<Book> bookListResponses = bookRedisService.getAllProduct(keyword, categoryId, pageRequest);
    //        if(bookListResponses == null){
    //            Page<Book> bookPage = getBookList(page,size,"id", keyword, categoryId);
    //            totalPage = bookPage.getTotalPages();
    //            bookListResponses = bookPage.getContent();
    //            bookRedisService.save(bookListResponses,keyword, categoryId, pageRequest);
    //        }
    //        return  bookListResponses;
    //    }
    //    public Page<Book> getBookList(int page, int size, String sortby, String keyword, int categoriId) throws
    // JsonProcessingException {
    //        Sort sort = Sort.by(sortby).descending();
    //        Pageable pageable = PageRequest.of(page, size, sort);
    //        Page<Book> bookListResponses = bookRepository.findByNameBookContainingAndCategoryList_IdCategory(keyword,
    // categoriId, pageable);
    ////        List<Book> bookList = bookListResponses.getContent();
    //        return  bookListResponses;
    //
    //    }
    @Override
    public ResponseEntity<?> save(JsonNode jsonNode) throws JsonProcessingException {
        try {
            Book book = objectMapper.treeToValue(jsonNode, Book.class);
            System.out.println(book);
            // Lưu thể loại sách
            List<Integer> idcategoryList =
                    objectMapper.readValue(jsonNode.get("idGenres").traverse(), new TypeReference<List<Integer>>() {});
            List<Category> categoryList = new ArrayList<>();
            for (int idCat : idcategoryList) {
                Optional<Category> category = categoryRepository.findById(idCat);
                categoryList.add(category.get());
            }
            book.setCategoryList(categoryList);
            // Lưu trước để lấy id sách đặt tên cho ảnh
            Book newBook = bookRepository.save(book);
            // neeus cos ma giam gia
            if (book.getDiscountPercent() != 0) {
                int sellPrice =
                        (int) ((int) book.getListPrice() - Math.round(book.getListPrice()) / book.getDiscountPercent());
                book.setSellPrice(sellPrice);
            }
            // Lưu thumbnail cho ảnh
            String datathumbnail = formatStringByJson(String.valueOf((jsonNode.get("thumbnail"))));

            // Lưu ảnh
            Image thumbnail = new Image();
            thumbnail.setBook(newBook);
            // thumbnail.setDataImg(formatStringByJson(String.valueOf((jsonNode.get("thumbnail")))));
            thumbnail.setIcon(true);
            MultipartFile multipartFile = Base64MuiltipartFileConverter.convert(datathumbnail);
            String thumbnailUrl = imageService.uploadImage(multipartFile, "Book_" + newBook.getIdBook());
            thumbnail.setLinkImg(thumbnailUrl);
            List<Image> imageList = new ArrayList<>();
            imageList.add(thumbnail);
            // Lưu ảnh có liên quan
            String dataReleatedImg = formatStringByJson(String.valueOf((jsonNode.get("relatedImg"))));
            List<String> arrDataReleatedImg =
                    objectMapper.readValue(jsonNode.get("relatedImg").traverse(), new TypeReference<List<String>>() {});
            for (int i = 0; i < arrDataReleatedImg.size(); i++) {
                String img = arrDataReleatedImg.get(i);
                Image image = new Image();
                image.setBook(newBook);
                image.setIcon(false);
                MultipartFile relatedImageFile = Base64MuiltipartFileConverter.convert(img);
                String imgUrl = imageService.uploadImage(relatedImageFile, "Book_" + newBook.getIdBook() + "." + i);
                image.setLinkImg(imgUrl);
                imageList.add(image);
            }
            newBook.setImageList(imageList);
            // Cap nhat lai anh
            bookRepository.save(newBook);
            BookListResponse bookListResponse = modelMapper.map(newBook, BookListResponse.class);
            bookElkRepository.save(bookListResponse);
            return ResponseEntity.ok("Success!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> update(JsonNode jsonNode) throws JsonProcessingException {
        try {
            Book book = objectMapper.treeToValue(jsonNode, Book.class);
            List<Image> imageList = imageRepository.findImagesByBook(book);
            // Luu the loai sach
            List<Integer> idCategories =
                    objectMapper.readValue(jsonNode.get("idGenres").traverse(), new TypeReference<List<Integer>>() {});
            List<Category> categoryList = new ArrayList<>();
            for (int idCat : idCategories) {
                Optional<Category> category = categoryRepository.findById(idCat);
                categoryList.add(category.get());
            }
            book.setCategoryList(categoryList);
            // Kieem tra xem thumbnail co thay doi khong
            String datathumbnail = formatStringByJson(String.valueOf(jsonNode.get("thumbnail")));
            if (Base64MuiltipartFileConverter.isBase64(datathumbnail)) {
                for (Image image : imageList) {
                    if (image.isIcon()) {
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
            List<String> arrDataImage =
                    objectMapper.readValue(jsonNode.get("relateImg").traverse(), new TypeReference<List<String>>() {});
            // Xem có xoá tất ở bên FE không
            boolean isCheckDelete = true;
            for (String img : arrDataImage) {
                if (!Base64MuiltipartFileConverter.isBase64(img)) {
                    isCheckDelete = false;
                }
            }
            // Neu xoa het tat ca
            if (isCheckDelete) {
                imageRepository.deleteImagesWithFalseThumbnailByBookId(newbook.getIdBook());
                Image thubnailTemp = imageList.get(0);
                imageList.clear();
                imageList.add(thubnailTemp);
                for (int i = 0; i < arrDataImage.size(); i++) {
                    String img = arrDataImage.get(i);
                    Image image = new Image();
                    image.setBook(newbook);
                    image.setIcon(false);
                    MultipartFile multipartFile = Base64MuiltipartFileConverter.convert(img);
                    String imgUrl = imageService.uploadImage(multipartFile, "Book_" + newbook.getIdBook() + "." + i);
                    image.setLinkImg(imgUrl);
                    imageList.add(image);
                }
            } else {
                // Neu khong xoa het tat ca va giu nguyen anh
                for (int i = 0; i < arrDataImage.size(); i++) {
                    String img = arrDataImage.get(i);
                    if (Base64MuiltipartFileConverter.isBase64(img)) {
                        Image image = new Image();
                        image.setBook(newbook);
                        image.setIcon(false);
                        MultipartFile multipartFile = Base64MuiltipartFileConverter.convert(img);
                        String imgUrl =
                                imageService.uploadImage(multipartFile, "Book_" + newbook.getIdBook() + "." + i);
                        image.setLinkImg(imgUrl);
                        imageRepository.save(image);
                    }
                }
            }
            newbook.setImageList(imageList);
            bookRepository.save(newbook);
            BookListResponse bookListResponse = modelMapper.map(newbook, BookListResponse.class);
            bookElkRepository.save(bookListResponse);
            return ResponseEntity.ok("SUCCESS!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public long getTotalBook() {
        return bookRepository.count();
    }

    @Override
    public PageResponse<?> getAll(int pageNo, int pageSize, String sortBy) {
        Sort sort = Sort.by(sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Book> books = bookRepository.findAll(pageable);
        List<Book> bookList = books.getContent();
        return PageResponse.<Book>builder()
                .page(books.getTotalPages())
                .total(books.getTotalElements())
                .rows(bookList)
                .size(books.getSize())
                .build();
    }

    @Override
    public ResponseEntity<?> getAlla() {
        List<Book> books = bookRepository.findAll();
        List<BookListResponse> bookListResponse =
                books.stream().map(book -> convertToResponse(book)).collect(Collectors.toList());
        bookElkRepository.saveAll(bookListResponse);
        return ResponseEntity.ok(bookListResponse);
    }

    public BookListResponse convertToResponse(Book book) {
        return BookListResponse.builder()
                .idBook(book.getIdBook())
                .nameBook(book.getNameBook())
                .author(book.getAuthor())
                .ISBN(book.getISBN())
                .listPrice(book.getListPrice())
                .sellPrice(book.getSellPrice())
                .quantity(book.getQuantity())
                .description(book.getDescription())
                .avgRating(book.getAvgRating())
                .soldQuantity(book.getSoldQuantity())
                .discountPercent(book.getDiscountPercent())
                .categoryList(book.getCategoryList().stream()
                        .map(this::convertCategory)
                        .collect(Collectors.toList()))
                .build();
    }

    private CategoriesResponse convertCategory(Category category) {
        return new CategoriesResponse(category.getIdCategory(), category.getNameCategory());
    }

    public String formatStringByJson(String json) {
        return json.replace("\"", "");
    }
}
