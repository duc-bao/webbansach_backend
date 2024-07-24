package vn.ducbao.springboot.webbansach_backend.security;

public class Endpoints {
    public static final String endpoint_fe = "http://localhost:5906";
    public static final String[] PUBLIC_GET_ENDPOINT = {
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
        "/users/*/orderList",
        "/users/*/roleList",
        "/users/*",
        "/review/**",
        "/reviews/*/user",
        "/order-details/*/book",
        "/favorite-book/get-favorite-book/*",
        "/feedbacks/**",
        "/vnpay/**",
        "/cart-item/get/*"
    };
    public static final String[] PUBLIC_POST_ENDPOINT = {
        "/user/register",
        "/user/login",
        "/book/add-book/**",
        "/review/add-review/**",
        "/order/add-order",
        "/cart-item/add-cart",
        "/favorite-book/add-favorite",
        "/feedback/add-feedback/**",
        "/vnpay/create-payment/**",
        "/api/user/logout",
        "/api/user/refresh-token",
        "/cart-item/add/*"
    };
    public static final String[] PUBLIC_DELETE_ENDPOINT = {
        "/cart-items/**", "/favorite-book/delete-book", "/cart-items/delete",
    };
    public static final String[] PUBLIC_PUT_ENDPOINT = {
        "/user/forgot-password",
        "/user/change-password",
        "/user/update-profile",
        "/user/change-avatar",
        "/review/update-review",
        "/order/update-order",
        "/cart-item/update-cart",
        "/cart-item/update/*/*"
    };
    public static final String[] ADMIN_ENDPOINT = {
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
        "/feedback/update-feedback/**",
        "/book/get-total/**",
        "/feedbacks/search/countBy/**",
        "/review/**",
        "/**",
    };
}
