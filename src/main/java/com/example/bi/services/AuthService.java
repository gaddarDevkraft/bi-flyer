package com.example.bi.services;

import com.example.bi.entity.BiFlyerUser;
import com.example.bi.model.SignUpRequest;
import com.example.bi.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepo userRepository;
    private final Keycloak keycloakAdmin;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.default-role:role-user}")
    private String defaultRole;

    @Transactional
    public void signup(SignUpRequest request) {
        log.info("Starting signup process for user: {}", request.getUsername());

        // Check if user already exists in database
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("User with email " + request.getEmail() + " already exists");
        }

        String userId = null;
        try {
            // 1. Create user in Keycloak
            UserRepresentation user = new UserRepresentation();
            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            user.setEmailVerified(false);
            user.setEnabled(true);

            Response response = keycloakAdmin.realm(realm).users().create(user);
            log.info("Keycloak user creation response status: {} for realm: {}", response.getStatus(), realm);

//            if (response.getStatus() != HttpStatus.CREATED.value()) {
//                String errorMessage = response.readEntity(String.class);
//                log.error("User creation failed in Keycloak: {}", errorMessage);
//                throw new RuntimeException("User creation failed in Keycloak: " + errorMessage);
//            }

            userId = CreatedResponseUtil.getCreatedId(response);
            log.info("User created in Keycloak with ID: {}", userId);

            // 2. Set password for the user
            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(request.getPassword());
            credential.setTemporary(false);

            keycloakAdmin.realm(realm)
                    .users()
                    .get(userId)
                    .resetPassword(credential);

            log.info("Password set for user: {}", userId);

            // 3. Assign default role
            try{
                RoleRepresentation role = keycloakAdmin.realm(realm)
                        .roles()
                        .get("role-user")
                        .toRepresentation();

                log.info("role is assigned {}", role.getName());
                keycloakAdmin.realm(realm)
                        .users()
                        .get(userId)
                        .roles()
                        .realmLevel()
                        .add(Collections.singletonList(role));
                log.info("assign role at the last");
            }catch (Exception e){
                e.fillInStackTrace();
                log.info("exception in role : {}", e.getMessage());
            }

            log.info("Role '{}' assigned to user: {}", defaultRole, userId);

            // 4. Save user in database
            BiFlyerUser entity = new BiFlyerUser();
            entity.setUserName(request.getUsername());
            entity.setEmail(request.getEmail());
            entity.setKeycloakUserId(userId);
            entity.setRoleAssign("user");

            userRepository.save(entity);
            log.info("User saved in database: {}", request.getUsername());

        } catch (Exception exception) {
            log.error("Error during signup process: {}", exception.getMessage(), exception);

            // Rollback: Delete user from Keycloak if database save fails
            if (userId != null) {
                try {
                    keycloakAdmin.realm(realm).users().delete(userId);
                    log.info("Rolled back: Deleted user from Keycloak: {}", userId);
                } catch (Exception rollbackException) {
                    log.error("Failed to rollback Keycloak user creation: {}", rollbackException.getMessage());
                }
            }

            throw new RuntimeException("Signup failed: " + exception.getMessage(), exception);
        }
    }

    public void forgotPassword(String email) {
        log.info("Processing forgot password request for email: {}", email);

        try {
            List<UserRepresentation> usersList = keycloakAdmin.realm(realm)
                    .users()
                    .search(email, true); // exact match

            if (usersList.isEmpty()) {
                throw new RuntimeException("User with email " + email + " not found");
            }

            UserRepresentation user = usersList.get(0);

            // Send password reset email
            keycloakAdmin.realm(realm)
                    .users()
                    .get(user.getId())
                    .executeActionsEmail(Collections.singletonList("UPDATE_PASSWORD"));

            log.info("Password reset email sent to: {}", email);

        } catch (Exception exception) {
            log.error("Error during forgot password process: {}", exception.getMessage(), exception);
            throw new RuntimeException("Failed to process forgot password request: " + exception.getMessage(), exception);
        }
    }

    public BiFlyerUser getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

//    public BiFlyerUser getUserByKeycloakId(String keycloakUserId) {
//        return userRepository.findByKeycloakUserId(keycloakUserId)
//                .orElseThrow(() -> new RuntimeException("User not found with Keycloak ID: " + keycloakUserId));
//    }
}