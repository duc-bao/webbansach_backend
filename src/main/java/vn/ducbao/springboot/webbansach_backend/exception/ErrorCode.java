package vn.ducbao.springboot.webbansach_backend.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {
        INVALID_KEYORFILTERELK(1001, "KEY OR FILED ELASTICSEARCH NOT VALID", HttpStatus.BAD_REQUEST),
        INVALID_USER(1002,"USER MUST BE AT LEAST 3 CHARACTER", HttpStatus.BAD_REQUEST)
    ;
    int code;
    String message;
    HttpStatus httpStatus;

    ErrorCode(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
