package com.interstellar.equipmentmanager.model.dto.team.out;

import com.interstellar.equipmentmanager.model.dto.user.out.UserCroppedDTO;
import lombok.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class TeamDTO {

    private UUID id;

    private String teamName;

    private UserCroppedDTO owner;

    private List<UserCroppedDTO> members;


}