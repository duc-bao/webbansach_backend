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
            "/orders/**",
            "/users/search/existsByUsername/**",
            "/users/search/existsByEmail/**",
            "/user/active-account/**",
            "/roles/**",
            "/users",
            "/users/**",
            "/order/**",
            "/order-detail/**",
            "/orders/**",
            "/cart-items/**",
    };
    public  static  final String[] PUBLIC_POST_ENDPOINT = {
            "/user/register",
            "/user/login",
            "/book/add-book/**",
    };
    public  static  final String [] PUBLIC_DELETE_ENDPOINT ={
            "/books",
            "/books/**",
    };

    public  static  final  String[] ADMIN_ENDPOINT = {

            "/user/add-user/**",
            "/book/get-total/**",
            "/**",
    };

}
