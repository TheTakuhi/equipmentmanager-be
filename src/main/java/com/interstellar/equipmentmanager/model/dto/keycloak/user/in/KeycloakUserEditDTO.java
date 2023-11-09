package com.interstellar.equipmentmanager.model.dto.keycloak.user.in;

import com.interstellar.equipmentmanager.model.enums.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class KeycloakUserEditDTO {
    @NotNull
    private List<UserRole> userRoles;
}
