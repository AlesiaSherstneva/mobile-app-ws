package com.develop.app.ws.security;

import com.develop.app.ws.SpringApplicationContext;
import org.springframework.core.env.Environment;

public class SecurityConstants {
    public static final long EXPIRATION_TIME = 864_000_000; // 10 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/users";
    // public static final String TOKEN_SECRET = "zpakx6c09be13f4l2s9glofjgm0rsbovvvqsej2xthhj9j69nfnsjp18qya8ttpa";

    public static String getTokenSecret() {
        Environment environment = (Environment) SpringApplicationContext.getBean("environment");
        return environment.getProperty("token.secret");
    }
}