package com.example.bi.utils;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class AppConstant {

    @Value("${keycloak.server-url}")
    static String keycloakBaseUrl;

    public static final String SERVER_ERROR = "Something went wrong!";
    public static final String KEYCLOAK_REALM_AUTH_URL = "https://kc-genaiapps.indegene.com/auth/realms/BI-flyer-automation/protocol/openid-connect";
}
