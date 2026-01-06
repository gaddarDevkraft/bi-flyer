package com.example.bi.utils;

import com.example.bi.model.BaseResponse;
import com.example.bi.model.LoginRequestModel;
import com.example.bi.model.LoginResponseModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class KeyCloakUtils {

    @Value("${keyclock.client.id}")
    private String clientId;

    @Value("${keyclock.client.secret}")
    private String clientSecret;


    public BaseResponse<LoginResponseModel> login(LoginRequestModel loginRequestModel){
        BaseResponse<LoginResponseModel> loginResponse = new BaseResponse<>();
        RestTemplate restTemplate = new RestTemplate();

        try{
            String loginUrl = AppConstant.KEYCLOAK_REALM_AUTH_URL + "/token";
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
        }catch (Exception exception){
            return new BaseResponse<>(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstant.SERVER_ERROR);
        }
    }
}
