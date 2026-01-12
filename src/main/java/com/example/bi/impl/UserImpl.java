package com.example.bi.impl;

import com.example.bi.entity.BiFlyerUser;
import com.example.bi.entity.Role;
import com.example.bi.model.*;
import com.example.bi.repo.RoleRepository;
import com.example.bi.repo.UserRepo;
import com.example.bi.services.UserService;
import com.example.bi.utils.AppConstant;
import com.example.bi.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserImpl implements UserService {

    private final RoleRepository roleRepository;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtil;

    @Override
    public BaseResponse<String> signInUser(SignUpRequestModel signUpRequestModel) {
        try {
            BiFlyerUser biFlyerUser = new BiFlyerUser();
            Role role = roleRepository.findByRoleNameIgnoreCase(signUpRequestModel.getRole())
                    .orElseThrow(() -> new RuntimeException("Role not found in DB"));

            biFlyerUser.setUserName(signUpRequestModel.getUsername());
            biFlyerUser.setFirstName(signUpRequestModel.getFirstname());
            biFlyerUser.setLastName(signUpRequestModel.getLastname());
            biFlyerUser.setEmail(signUpRequestModel.getEmail());
            biFlyerUser.setPassword(passwordEncoder.encode(signUpRequestModel.getPassword()));
            biFlyerUser.setKeycloakUserId(UUID.randomUUID().toString());

            biFlyerUser.setRole(role);

            userRepo.save(biFlyerUser);
            return new BaseResponse<>("user created successfully", HttpStatus.CREATED.value(), "success");
        } catch (Exception exception) {
            log.info("failed signup exception : {}", exception.getMessage());
            return new BaseResponse<>(null, 500, AppConstant.SERVER_ERROR);
        }
    }

    @Override
    public BaseResponse<LoginResponseModel> loginUser(LoginRequestModel loginRequestModel) {
        try {
            BiFlyerUser user = userRepo
                    .findByEmailAndIsDeletedFalse(loginRequestModel.getEmail())
                    .orElseThrow(() -> new RuntimeException("Invalid email or password"));

            if (!passwordEncoder.matches(loginRequestModel.getPassword(), user.getPassword())) {
                throw new RuntimeException("Invalid email or password");
            }

            String accessToken = jwtUtil.generateAccessToken(user.getEmail());
            String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

            LoginResponseModel response = new LoginResponseModel();
            response.setAccess_token(accessToken);
            response.setExpires_in(jwtUtil.getAccessTokenExpiry());
            response.setRefresh_expires_in(jwtUtil.getRefreshTokenExpiry());
            response.setRefresh_token(refreshToken);
            response.setToken_type("Bearer");
            response.setNot_before_policy(0);
            response.setSession_state(UUID.randomUUID().toString());
            response.setScope("profile email");
            response.setUser_name(user.getUserName());
            response.setRoleName(user.getRole().getDisplayName());
            response.setTemporary(false);
            response.setEmailVerified(true); // or from DB if available

            return new BaseResponse<>(response, HttpStatus.OK.value(), "success");
        } catch (Exception exception) {
            log.info("failed login exception : {}", exception.getMessage());
            return new BaseResponse<>(null, 500, AppConstant.SERVER_ERROR);
        }

    }

    @Override
    public BaseResponse<String> forgetPassword(ForgetPasswordRequest forgetPasswordRequest) {
        try {
            BiFlyerUser user = userRepo
                    .findByEmailAndIsDeletedFalse(forgetPasswordRequest.getUsername())
                    .orElseThrow(() -> new RuntimeException("Invalid email or password"));
            String password = "";
            if (user != null){
                // get decoded password
            }
            return new BaseResponse<>(password, HttpStatus.OK.value(), "success");
        } catch (Exception exception) {
            log.info("failed forget password exception : {}", exception.getMessage());
            return new BaseResponse<>(null, 500, AppConstant.SERVER_ERROR);
        }
    }
}
