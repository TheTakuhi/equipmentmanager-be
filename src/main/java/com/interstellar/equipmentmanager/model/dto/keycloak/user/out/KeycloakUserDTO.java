package com.interstellar.equipmentmanager.model.dto.keycloak.user.out;

import com.interstellar.equipmentmanager.model.enums.UserRole;
import lombok.*;

import java.util.List;
import java.util.UUID;

/*
    This DTO is used as a representation for user from keycloak.
    It is used for mapping keycloak user model to our user model.
 */

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class KeycloakUserDTO {
    private UUID keycloakId;
    private UUID ldapId;
    private String login;
    private String firstName;
    private String lastName;
    private String email;
    private String personalNumber;
    private String photo; //base64 photo
    private List<UserRole> userRoles;
}