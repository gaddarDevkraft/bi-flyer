package com.example.bi.controllers;

import com.example.bi.impl.KeyCloakImpl;
import com.example.bi.model.BaseResponse;
import com.example.bi.model.ForgetPasswordRequest;
import com.example.bi.model.LoginRequestModel;
import com.example.bi.model.LoginResponseModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class PublicController {

    @Autowired
    private KeyCloakImpl keyCloak;

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<LoginResponseModel>> login(@RequestBody LoginRequestModel loginRequestModel){
        BaseResponse<LoginResponseModel> baseResponse = keyCloak.login(loginRequestModel);
        return new ResponseEntity<>(baseResponse, HttpStatus.resolve(baseResponse.getStatus()));
    }

    @PostMapping("/forget-password")
    public ResponseEntity<BaseResponse<String>> forgetPassword(@RequestBody ForgetPasswordRequest forgetPasswordRequest){
        System.out.println(forgetPasswordRequest);
        BaseResponse<String> baseResponse = keyCloak.forgetPassword(forgetPasswordRequest);
        return new ResponseEntity<>(baseResponse,  HttpStatus.resolve(baseResponse.getStatus()));
    }
}
