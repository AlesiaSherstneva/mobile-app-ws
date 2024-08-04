package com.develop.app.ws.security;

public class SecurityConstants {
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/users";
    public static final String TOKEN_SECRET = "zpakx6c09be13f4l2s9glofjgm0rsbovvvqsej2xthhj9j69nfnsjp18qya8ttpa";
}