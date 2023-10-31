package com.interstellar.equipmentmanager.model.dto.UserDTO;

import com.interstellar.equipmentmanager.model.dto.AuditDTO;
import com.interstellar.equipmentmanager.model.dto.ContractCroppedDTO;
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
public class UserDTO {
    private UUID id;
    private UUID ldapId;
    private String login;
    private String email;
    private String firstName;
    private String lastName;
    private String fullName;
    private String photo;
    private List<UserRole> userRoles;
    private List<ContractCroppedDTO> ownedContracts;
    private AuditDTO auditInfo;
    private Boolean removed;
}
