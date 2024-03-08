package com.interstellar.equipmentmanager.model.dto.team.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class TeamEditDTO {
    
    @NotBlank(message = "Team name is mandatory")
    private String teamName;
    
    @NotNull(message = "Owner id is mandatory")
    private UUID ownerId;
    
    @NotNull(message = "Members ids are not allowed to be null")
    private List<UUID> membersIds;
}
