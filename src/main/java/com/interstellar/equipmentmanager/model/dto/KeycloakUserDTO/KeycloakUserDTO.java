package com.interstellar.equipmentmanager.model.dto.KeycloakUserDTO;

import com.interstellar.equipmentmanager.model.enums.UserRole;
import lombok.*;

import java.util.List;
import java.util.UUID;

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