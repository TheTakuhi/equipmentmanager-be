package com.interstellar.equipmentmanager.model.dto.user.out;

import com.interstellar.equipmentmanager.model.enums.UserRole;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CurrentUserDTO {
    private UUID id;
    private String login;
    private String email;
    private String firstName;
    private String lastName;
    private String fullName;
    private String photo;
    private List<UserRole> userRoles;
}
