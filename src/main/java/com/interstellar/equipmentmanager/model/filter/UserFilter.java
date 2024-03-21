package com.interstellar.equipmentmanager.model.filter;

import com.interstellar.equipmentmanager.model.enums.UserRole;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserFilter {
    private String login;
    private String fullName;
    private List<UserRole> userRoles;
    private Boolean includeRemovedUser;
}
