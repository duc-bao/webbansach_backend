package vn.ducbao.springboot.webbansach_backend.service.image;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    String uploadImage(MultipartFile multipartFile, String name);

    void deleteImage(String imgUrl);
}
