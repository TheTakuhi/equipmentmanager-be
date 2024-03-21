package com.interstellar.equipmentmanager.model.dto.user.out;

import com.interstellar.equipmentmanager.model.enums.UserRole;
import lombok.*;

import java.util.List;
import java.util.UUID;

/*
    UserCroppedDTO is used to display only necessary information related to specific user.
 */

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserCroppedDTO {
    private UUID id;
    private String login;
    private String email;
    private String firstName;
    private String lastName;
    private String fullName;
    private String photo;
    private List<UserRole> userRoles;
    private Boolean removed;
    private List<UUID> teamsIds;
    private List<UUID> ownedTeamsIds;
    private List<UUID> loansIds;
    private List<UUID> ownedItemsIds;
    private List<UUID> borrowingsIds;
}
