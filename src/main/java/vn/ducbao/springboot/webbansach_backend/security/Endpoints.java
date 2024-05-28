package vn.ducbao.springboot.webbansach_backend.security;

public class Endpoints {
    public  static  final  String endpoint_fe = "http://localhost:3000";
    public  static  final  String[] PUBLIC_GET_ENDPOINT = {
            "/books",
            "/books/**",
            "/images",
            "/images/**",
            "/categories",
            "/categories/**",
            "/users/search/existsByUsername/**",
            "/users/search/existsByEmail/**",
            "/user/active-account/**",
    };
    public  static  final String[] PUBLIC_POST_ENDPOINT = {
            "/user/register",
            "/user/login"
    };

    public  static  final  String[] ADMIN_ENDPOINT = {
            "/user",
            "/users/**",
            "/**"
    };
    public static final String[] ADMIN_POST_ENDPOINT = {
            "/book/add-book/**",
    };
}
