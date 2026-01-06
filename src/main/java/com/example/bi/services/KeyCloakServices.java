package com.example.bi.services;

import com.example.bi.model.BaseResponse;
import com.example.bi.model.LoginRequestModel;
import com.example.bi.model.LoginResponseModel;

public interface KeyCloakServices {
    BaseResponse<LoginResponseModel> login(LoginRequestModel loginRequestModel);
}
