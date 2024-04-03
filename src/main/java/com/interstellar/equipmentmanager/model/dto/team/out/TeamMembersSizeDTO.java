package com.interstellar.equipmentmanager.model.dto.team.out;

import com.interstellar.equipmentmanager.model.dto.user.out.UserCroppedDTO;
import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeamMembersSizeDTO {
    private UUID id;
    private String teamName;
    private UserCroppedDTO owner;
    private int membersSize;
}
