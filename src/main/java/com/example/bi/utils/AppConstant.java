package com.example.bi.utils;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class AppConstant {

    /*common*/
    public static final String SERVER_ERROR = "Something went wrong!";

    /*jwt validation time set*/
    public static final String SECRET = "bi-flyer-super-secure-jwt-secret-key-256bit!!";
    public static final long ACCESS_TOKEN_EXP = 15 * 60 * 1000; // 15 min
    public static final long REFRESH_TOKEN_EXP = 7 * 24 * 60 * 60 * 1000; // 7 days


    /*Encryption key's*/
    public static final String SECRET_KEY = "EVA@!8*&5%GTS%#B";
    public static final String ALGORITHM = "AES";
    public static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    public static final  String INIT_VECTOR = "AWRTRETSGT5432TH";
}
