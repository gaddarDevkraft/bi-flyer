package com.example.bi.config;

import com.example.bi.utils.AppConstant;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeyCloakConfiguration {

    @Value("${keycloak.server-url}")
    private String serverUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keyclock.client.id}")
    private String clientId;

    @Value("${keyclock.client.secret}")
    private String clientSecret;

    @Autowired
    JwtConverter jwtConverter;

    @Bean
    public Keycloak keycloakAdmin() {
        System.out.println("server url : "+serverUrl+"\nrealm : "+realm+"\nclient id : "+clientId+"\nclient secret: "+clientSecret);
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .build();
    }
}

