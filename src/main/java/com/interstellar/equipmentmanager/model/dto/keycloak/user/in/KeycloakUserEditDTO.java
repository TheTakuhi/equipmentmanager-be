package com.interstellar.equipmentmanager.model.dto.keycloak.user.in;

import com.interstellar.equipmentmanager.model.enums.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

/*
    This DTO is used for updating user roles in keycloak.
    We can only update user roles since the rest of the data is synced from AD.
 */

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class KeycloakUserEditDTO {
    @NotNull
    private List<UserRole> userRoles;
}
