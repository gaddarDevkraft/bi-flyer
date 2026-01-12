package com.example.bi.controllers;


import com.example.bi.impl.UserImpl;
import com.example.bi.model.BaseResponse;
import com.example.bi.model.LoginRequestModel;
import com.example.bi.model.LoginResponseModel;
import com.example.bi.model.SignUpRequestModel;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/test-auth")
public class TestController {

    private final UserImpl userImpl;

    @PostMapping("/signup")
    public BaseResponse<String> signup(@RequestBody @Valid SignUpRequestModel signUpRequestModel){
        return userImpl.signInUser(signUpRequestModel);
    }

    @PostMapping("/login")
    public BaseResponse<LoginResponseModel> login(@RequestBody @Valid LoginRequestModel loginRequestModel){
        return userImpl.loginUser(loginRequestModel);
    }



}
