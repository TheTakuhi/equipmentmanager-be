package com.interstellar.equipmentmanager.model.dto.user.out;

import com.interstellar.equipmentmanager.model.dto.audit.AuditDTO;
import com.interstellar.equipmentmanager.model.dto.item.out.ItemCroppedDTO;
import com.interstellar.equipmentmanager.model.dto.loan.out.LoanCroppedDTO;
import com.interstellar.equipmentmanager.model.dto.team.out.TeamCroppedDTO;
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
    private String login;
    private String email;
    private String firstName;
    private String lastName;
    private String fullName;
    private String photo;
    private List<UserRole> userRoles;
    private AuditDTO auditInfo;
    private Boolean removed;
    private List<TeamCroppedDTO> teams;
    private List<TeamCroppedDTO> ownedTeams;
    private List<LoanCroppedDTO> loans;
    private List<LoanCroppedDTO> borrowings;
    private List<ItemCroppedDTO> ownedItems;
}