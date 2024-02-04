package com.interstellar.equipmentmanager.service.impl;

import com.interstellar.equipmentmanager.exception.ForbiddenAddException;
import com.interstellar.equipmentmanager.exception.ForbiddenRemoveException;
import com.interstellar.equipmentmanager.exception.ResourceNotFoundException;
import com.interstellar.equipmentmanager.model.dto.team.in.TeamCreateDTO;
import com.interstellar.equipmentmanager.model.dto.team.in.TeamEditDTO;
import com.interstellar.equipmentmanager.model.dto.team.out.TeamDTO;
import com.interstellar.equipmentmanager.model.dto.user.out.UserDTO;
import com.interstellar.equipmentmanager.model.entity.Loan;
import com.interstellar.equipmentmanager.model.entity.Team;
import com.interstellar.equipmentmanager.model.entity.User;
import com.interstellar.equipmentmanager.model.enums.UserRole;
import com.interstellar.equipmentmanager.model.filter.LoanSpecifications;
import com.interstellar.equipmentmanager.repository.TeamRepository;
import com.interstellar.equipmentmanager.security.service.UserAuthorizationService;
import com.interstellar.equipmentmanager.service.TeamService;
import com.interstellar.equipmentmanager.service.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final ModelMapper mapper;
    private final UserAuthorizationService userAuthorizationService;
    private final UserService userService;


    @Override
    public TeamDTO createTeam(TeamCreateDTO teamCreateDTO) {
        Team team = mapper.map(teamCreateDTO, Team.class);
        team.setOwner(mapper.map(userAuthorizationService.getCurrentUser(), User.class));
        team.setMembers(team.getMembers().stream().map(x -> userService.getOriginalUser(x.getId())).collect(Collectors.toList()));
        team.getMembers().forEach(x -> x.getTeams().add(team));
        return mapper.map(teamRepository.save(team), TeamDTO.class);
    }

    @Override
    public TeamDTO updateTeam(UUID id, TeamEditDTO teamEditDTO) {
        Team team = teamRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Team.class.getName(), "id", id.toString()));
        Team newTeam = mapper.map(teamEditDTO, Team.class);
        team.getMembers().forEach(member -> member.getTeams().remove(team));
        team.getMembers().clear();
        team.getMembers().addAll(newTeam.getMembers().stream().map(x -> userService.getOriginalUser(x.getId())).collect(Collectors.toList()));
        team.getMembers().forEach(member -> member.getTeams().add(team));
        if (newTeam.getOwner() != null) {
            User user = userService.getOriginalUser(newTeam.getOwner().getId());
            team.setOwner(user);
            user.getOwnedTeams().add(team);
        }
        team.setTeamName(newTeam.getTeamName());
        return mapper.map(teamRepository.save(team), TeamDTO.class);
    }

    @Override
    public TeamDTO addUserToTeam(UUID teamId, UUID userId) {
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new ResourceNotFoundException(Team.class.getName(), "id", teamId.toString()));
        User user = userService.getOriginalUser(userId);
        if (team.getMembers().contains(user)) {
            throw new ForbiddenAddException(userId.toString(), teamId.toString());
        }
        team.getMembers().add(user);
        user.getTeams().add(team);

        return mapper.map(teamRepository.save(team), TeamDTO.class);
    }

    @Override
    public TeamDTO removeUserFromTeam(UUID teamId, UUID userId) {
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new ResourceNotFoundException(Team.class.getName(), "id", teamId.toString()));
        User user = userService.getOriginalUser(userId);
        if (!team.getMembers().contains(user)) {
            throw new ForbiddenRemoveException(userId.toString(), teamId.toString());
        }
        team.getMembers().remove(user);
        user.getTeams().remove(team);
        return mapper.map(teamRepository.save(team), TeamDTO.class);
    }

    //todo filter teamMebers

    @Override
    public TeamDTO getTeamById(UUID teamId) {
        return mapper.map(teamRepository.findById(teamId).orElseThrow(() -> new ResourceNotFoundException(Team.class.getName(), "id", teamId.toString())), TeamDTO.class);

    }

    @Override
    public Page<TeamDTO> getAllTeams(Pageable pageable) {
        if(userAuthorizationService.getCurrentUser().getUserRoles().contains(UserRole.ADMIN)){
            return teamRepository.findAll(pageable).map((element) -> mapper.map(element, TeamDTO.class));
        }
        return teamRepository.findByMembersId(userAuthorizationService.getCurrentUser().getId(),pageable).map((element) -> mapper.map(element, TeamDTO.class));
    }


    @Override
    public Boolean isOwner(UUID id) {

        if (getTeamById(id).getOwner().getId().equals(userAuthorizationService.getCurrentUser().getId())) {
            return true;
        } else return false;
    }

    @Override
    @Transactional
    public void deleteTeamById(UUID teamId) {
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new ResourceNotFoundException(Team.class.getName(), "id", teamId.toString()));
        team.getMembers().forEach(member -> member.getTeams().remove(team));
        teamRepository.deleteById(team.getId());
    }
}