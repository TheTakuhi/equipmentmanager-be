package com.interstellar.equipmentmanager.model.dto.team.in;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    
    @NotBlank(message = "Team name is mandatory")
    private String teamName;
    
    @NotNull(message = "Members ids are not allowed to be null")
    private List<UUID> membersIds;
    
    @NotNull(message = "Owner id is mandatory")
    private UUID ownerId;
}
