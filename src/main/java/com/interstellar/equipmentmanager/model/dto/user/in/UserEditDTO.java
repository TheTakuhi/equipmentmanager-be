package com.interstellar.equipmentmanager.model.dto.user.in;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.interstellar.equipmentmanager.annotation.AlphaString;
import com.interstellar.equipmentmanager.model.entity.AuditInfo;
import com.interstellar.equipmentmanager.model.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserEditDTO {

    @JsonIgnore
    private UUID id;

    @JsonIgnore
    private String login;

    @JsonIgnore
    @Email
    private String email;

    @JsonIgnore
    private String photo;

    @JsonIgnore
    @AlphaString
    private String firstName;

    @JsonIgnore
    @AlphaString
    private String lastName;

    @NotEmpty(message = "UserRoles is a mandatory field")
    private List<UserRole> userRoles;

    @JsonIgnore
    private AuditInfo auditInfo;
}