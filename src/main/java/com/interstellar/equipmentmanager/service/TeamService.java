package com.interstellar.equipmentmanager.service;

import com.interstellar.equipmentmanager.model.dto.team.in.TeamCreateDTO;
import com.interstellar.equipmentmanager.model.dto.team.in.TeamEditDTO;
import com.interstellar.equipmentmanager.model.dto.team.out.TeamDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface TeamService {


    TeamDTO createTeam(TeamCreateDTO teamCreateDTO);

    TeamDTO updateTeam(UUID id, TeamEditDTO teamEditDTO);

    TeamDTO addUserToTeam(UUID userId, UUID teamId);

    TeamDTO removeUserFromTeam(UUID teamId, UUID userId);

    TeamDTO getTeamById(UUID id);

    Page<TeamDTO> getAllTeams(Pageable pageable);

    Boolean isOwner(UUID id);

    void deleteTeamById(UUID teamId);
}