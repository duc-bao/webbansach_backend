package vn.ducbao.springboot.webbansach_backend.security;

public class Endpoints {
    public  static  final  String endpoint_fe = "http://localhost:3000";
    public  static  final  String[] PUBLIC_GET_ENDPOINT = {
            "/books",
            "/books/**",
            "/images",
            "/images/**",
            "/categories",
            "/users/search/existsByUserName/**",
            "/users/search/existsByEmail/**"
    };
    public  static  final String[] PUBLIC_POST_ENDPOINT = {
            "/user/register",
    };
    public  static  final  String[] ADMIN_ENDPOINT = {
            "/user",
            "/users/**"
    };
}
