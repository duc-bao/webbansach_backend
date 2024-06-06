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
            "/cart-items/**",
            "/users/*/listCartItems",
            "/users/*/listOrders",
            "/users/*/roleList",
            "/users/*",
            "/review/**",
    };
    public  static  final String[] PUBLIC_POST_ENDPOINT = {
            "/user/register",
            "/user/login",
            "/book/add-book/**",
            "/review/add-review/**",
            "/order/add-order",
    };
    public  static  final String [] PUBLIC_DELETE_ENDPOINT ={

    };
    public static  final String [] PUBLIC_PUT_ENDPOINT = {
            "/user/forgot-password",
            "/user/change-password",
            "/user/update-profile",
            "/user/change-avatar",
            "/review/update-review"
    };
    public  static  final  String[] ADMIN_ENDPOINT = {
            "/cart-items/**",
            "/roles/**",
            "/user/add-user/**",
            "/users",
            "/users/**",
            "/book/get-total/**",
            "/order/**",
            "/order-detail/**",
            "/orders/**",
            "/books",
            "/books/**",
            "/**",

    };

}
