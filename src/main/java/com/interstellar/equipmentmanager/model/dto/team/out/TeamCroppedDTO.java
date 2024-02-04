package com.interstellar.equipmentmanager.model.dto.team.out;

import lombok.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class TeamCroppedDTO {

    private UUID id;

    private String teamName;

    private UUID ownerId;

    private List<UUID> membersIds;

}