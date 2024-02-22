package com.interstellar.equipmentmanager.model.dto.user.in;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.interstellar.equipmentmanager.annotation.AlphaString;
import com.interstellar.equipmentmanager.model.entity.AuditInfo;
import com.interstellar.equipmentmanager.model.enums.UserRole;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

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

    @Size(max = 7, min = 7, message = "Login must be 7 characters long")
    private String login;

    @Email
    @NotBlank(message = "Email is mandatory")
    private String email;

    private String photo;

    @AlphaString
    @NotBlank(message = "First name is mandatory")
    private String firstName;

    @AlphaString
    @NotBlank(message = "Last name is mandatory")
    private String lastName;

    @NotEmpty(message = "UserRoles cannot be empty")
    private List<UserRole> userRoles = new ArrayList<>(List.of(UserRole.GUEST));

    @JsonIgnore
    private AuditInfo auditInfo;

    @JsonIgnore
    private boolean removed = false;
}