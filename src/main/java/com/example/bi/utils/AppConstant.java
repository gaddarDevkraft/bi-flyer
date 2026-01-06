package com.example.bi.utils;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class AppConstant {
    public static final String RELM = "pm";
    public static final String CLIENT_ID = "realm-flyer";
    public static final String KEYCLOAK_BASE_URL = "http://localhost:9090";


    public static final String SERVER_ERROR = "Something went wrong!";
    public static final String KEYCLOAK_REALM_AUTH_URL = "http://localhost:9090/realms/pm/protocol/openid-connect";
}
