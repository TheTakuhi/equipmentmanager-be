package com.interstellar.equipmentmanager.model.dto.team.in;

import lombok.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class TeamEditDTO {

    private String teamName;

    private UUID ownerId;

    private List<UUID> membersIds;
}
