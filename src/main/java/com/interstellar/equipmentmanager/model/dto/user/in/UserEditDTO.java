package com.interstellar.equipmentmanager.model.dto.user.in;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.interstellar.equipmentmanager.annotation.AlphaString;
import com.interstellar.equipmentmanager.model.entity.AuditInfo;
import com.interstellar.equipmentmanager.model.enums.UserRole;
import jakarta.validation.constraints.Email;
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

    private List<UserRole> userRoles;

    @JsonIgnore
    private AuditInfo auditInfo;

    public boolean nonCriticalInfoMissing() {
        return firstName == null || photo == null;
    }

    public boolean criticalInfoMissing() {
        return id == null ||  login == null || email == null || lastName == null;
    }
}
