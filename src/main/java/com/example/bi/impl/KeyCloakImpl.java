package com.example.bi.impl;

import com.example.bi.model.BaseResponse;
import com.example.bi.model.LoginRequestModel;
import com.example.bi.model.LoginResponseModel;
import com.example.bi.services.KeyCloakServices;
import com.example.bi.utils.AppConstant;
import com.example.bi.utils.KeyCloakUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class KeyCloakImpl implements KeyCloakServices {

    @Autowired
    KeyCloakUtils keyClockUtil;

    @Override
    public BaseResponse<LoginResponseModel> login(LoginRequestModel loginRequestModel) {
        try {
            return keyClockUtil.login(loginRequestModel);
        } catch (Exception e) {
            return new BaseResponse<>(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstant.SERVER_ERROR);
        }
    }
}
