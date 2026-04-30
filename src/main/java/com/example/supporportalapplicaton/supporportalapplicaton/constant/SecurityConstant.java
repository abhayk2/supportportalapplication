package com.example.supporportalapplicaton.supporportalapplicaton.constant;

public class SecurityConstant {
    public static final long EXPIRATION_TIME = 432_000_000;//5 days
    public static final String TOKEN_PREFIX = "Bearer ";
//    public static final String JWT_TOKEN_HEADER = "JWT-Token";
    public static final String TOKEN_CANNOT_BE_VERIFIED = "Token cannot be verified";
    public static final String GET_ARRAYS_LLC = "Get arrays LLC";
    public static final String GET_ARRAYS_ADMINISTRATION = "User Management Portal";
    public static final String AUTHORITIES = "authorities";
    public static final String FORBIDDEN_MESSAGE = "You don't have permission to access this resource";
    public static final String ACCESS_DENIED_MESSAGE = "Access Denied";
    public static final String OPTIONS_HTTP_METHOD = "OPTIONS";
    //public static final String[]  PUBLIC_URLS = {"/user/login","/user/register","/user/resetpassword/**","/user/image/**"};
    public static final String[] PUBLIC_URLS = {"**"};
}
