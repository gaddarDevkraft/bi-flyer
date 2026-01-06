package com.example.bi.model;

import lombok.Data;

@Data
public class LoginResponseModel {
    private String access_token;
    private int expires_in;
    private int refresh_expires_in;
    private String refresh_token;
    private String token_type;
    private int not_before_policy;
    private String session_state;
    private String scope;
    private String user_name;
    private String roleName;
    private boolean isTemporary;
    private Boolean emailVerified;
}