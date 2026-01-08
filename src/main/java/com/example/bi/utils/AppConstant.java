package com.example.bi.utils;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class AppConstant {

    public static final String SERVER_ERROR = "Something went wrong!";
}
