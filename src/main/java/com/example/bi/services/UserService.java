package com.example.bi.services;

import com.example.bi.model.*;


public interface UserService {
    BaseResponse<String> signInUser(SignUpRequestModel signUpRequestModel);
    BaseResponse<LoginResponseModel> loginUser(LoginRequestModel loginRequestModel);
    BaseResponse<String> forgetPassword(ForgetPasswordRequest forgetPasswordRequest);
}
