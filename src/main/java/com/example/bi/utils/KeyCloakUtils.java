package com.example.bi.utils;


import com.example.bi.model.BaseResponse;
import com.example.bi.model.LoginRequestModel;
import com.example.bi.model.LoginResponseModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.logging.Logger;

@Slf4j
@Component
public class KeyCloakUtils {

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keyclock.client.id}")
    private String clientId;

    @Value("${keyclock.client.secret}")
    private String clientSecret;

    @Value("${keycloak.server-url}")
    private String serverUrl;

    @Autowired
    private Keycloak keycloakAdmin;

    public BaseResponse<LoginResponseModel> login(LoginRequestModel loginRequestModel) {
        BaseResponse<LoginResponseModel> loginResponse = new BaseResponse<>();
        RestTemplate restTemplate = new RestTemplate();

        try {
            String loginUrl = serverUrl+"realms/"+realm+"/protocol/openid-connect/token";
            log.info("keyclock login url : {}", loginUrl);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
            map.add("username", loginRequestModel.getEmail());
            map.add("grant_type", "password");
            map.add("client_id", clientId);
            map.add("client_secret", clientSecret);
            map.add("password", loginRequestModel.getPassword());

            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, headers);
            ResponseEntity<String> response = restTemplate.exchange(loginUrl, HttpMethod.POST, request, String.class);
            System.out.println(response);

            if (response.getStatusCode() == HttpStatus.OK) {
                // Process the token response
                String stringJson = response.getBody().replaceAll("not-before-policy", "not_before_policy");
                ObjectMapper objectMapper = new ObjectMapper();
                LoginResponseModel json = objectMapper.readValue(stringJson, LoginResponseModel.class);
                json.setUser_name(loginRequestModel.getEmail());
                json.setRoleName("admin");
                loginResponse.setData(json);
                loginResponse.setStatus(HttpStatus.OK.value());
            } else if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                return new BaseResponse<>(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Invalid username or password!");
            }
            return loginResponse;
        } catch (Exception exception) {
            return new BaseResponse<>(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
        }
    }

    public BaseResponse<String> forgetPassword(String email) {
        try {
            System.out.println("forget password start");
            List<UserRepresentation> users = keycloakAdmin.realm(realm)
                    .users()
                    .searchByEmail(email, true);
            System.out.println("user has find");
            if (users.isEmpty()) {
                System.out.println("user not exits");
                return new BaseResponse<>(null, HttpStatus.BAD_GATEWAY.value(), "user not exit");
            }
            System.out.println("start sending the mail");
            keycloakAdmin.realm(realm)
                    .users()
                    .get(users.getFirst().getId())
                    .executeActionsEmail(List.of("UPDATE_PASSWORD"));
            System.out.println("mail has send to the user");
            return new BaseResponse<>(null, HttpStatus.OK.value(), "reset link has been sent");

        } catch (Exception e) {
            return new BaseResponse<>(null, HttpStatus.BAD_GATEWAY.value(), e.getMessage());
        }
    }

}
