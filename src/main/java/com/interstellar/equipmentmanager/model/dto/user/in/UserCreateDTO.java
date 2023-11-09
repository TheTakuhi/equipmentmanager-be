package com.interstellar.equipmentmanager.model.dto.user.in;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.interstellar.equipmentmanager.annotation.AlphaString;
import com.interstellar.equipmentmanager.model.entity.AuditInfo;
import com.interstellar.equipmentmanager.model.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDTO {
    @JsonIgnore
    private UUID id;

    @NotNull
    @NotBlank
    private String login;
    @Email
    @NotNull @NotBlank
    private String email;

    private String photo;

    @AlphaString
    @NotNull @NotBlank
    private String firstName;
    @AlphaString @NotNull @NotBlank
    private String lastName;

    @NotNull
    private List<UserRole> userRoles = new ArrayList<>(List.of(UserRole.GUEST));

    @JsonIgnore
    private AuditInfo auditInfo;

    @JsonIgnore
    private boolean removed = false;
}
