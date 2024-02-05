package com.interstellar.equipmentmanager.model.dto.team.in;

import lombok.*;

import java.util.List;
import java.util.UUID;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class TeamCreateDTO {

    private String teamName;

    private List<UUID> memberIds;

    private UUID ownerId;
}