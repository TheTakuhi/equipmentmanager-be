package com.interstellar.equipmentmanager.model.dto.user.out;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class UserHierarchyDTO {
    private UUID id;
    private String login;
    private String email;
    private String firstName;
    private String lastName;
    private String fullName;
    private String photo;
    private UserHierarchyDTO manager = null;
}
