package com.example.bi.services;

import com.example.bi.entity.BiFlyerUser;
import com.example.bi.model.SignUpRequest;
import com.example.bi.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepo userRepository;
    private final Keycloak keycloakAdmin;

    @Value("${keycloak.realm}")
    private String realm;

    public void signup(SignUpRequest request) {

        // 1. Create user in Keycloak
        try {
            UserRepresentation user = new UserRepresentation();
            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            user.setEnabled(true);

            Response response = keycloakAdmin.realm(realm).users().create(user);
            System.out.println("response : "+response.getStatus()+"realm : "+realm);
            /*if (response.getStatus() != 201) {
                throw new RuntimeException("User creation failed in Keycloak");
            }*/

            String userId = CreatedResponseUtil.getCreatedId(response);

            System.out.println("userId : "+userId);

            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(request.getPassword());
            credential.setTemporary(false);

            user.setCredentials(List.of(credential));

            // 2. Assign role
            System.out.println("going to role");
            RoleRepresentation role = keycloakAdmin.realm("pm")
                            .roles()
                            .get("role-user")
                            .toRepresentation();

            keycloakAdmin.realm("pm")
                    .users()
                    .get(userId)
                    .roles()
                    .realmLevel()
                    .add(List.of(role));

            System.out.println("save entry in db");
            // 3. Save in DB
            BiFlyerUser entity = new BiFlyerUser();
            entity.setUserName(request.getUsername());
            entity.setEmail(request.getEmail());
            entity.setKeycloakUserId(userId);
            entity.setRoleAssign("user");

            userRepository.save(entity);
        }catch (Exception exception){
            exception.printStackTrace();
            System.out.println(exception.getMessage());
        }
    }

    public void forgotPassword(String email) {
        UsersResource users = keycloakAdmin.realm(realm).users();
        List<UserRepresentation> usersList = users.search(email);

        if (usersList.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        users.get(usersList.getFirst().getId())
                .executeActionsEmail(List.of("UPDATE_PASSWORD"));
    }


}
