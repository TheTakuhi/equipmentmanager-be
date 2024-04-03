package com.interstellar.equipmentmanager.service.impl;

import com.interstellar.equipmentmanager.exception.ResourceConflictException;
import com.interstellar.equipmentmanager.exception.ResourceNotFoundException;
import com.interstellar.equipmentmanager.model.dto.CustomPageDTO;
import com.interstellar.equipmentmanager.model.dto.team.in.TeamCreateDTO;
import com.interstellar.equipmentmanager.model.dto.team.in.TeamEditDTO;
import com.interstellar.equipmentmanager.model.dto.team.out.TeamDTO;
import com.interstellar.equipmentmanager.model.dto.team.out.TeamMembersSizeDTO;
import com.interstellar.equipmentmanager.model.dto.user.out.UserDTO;
import com.interstellar.equipmentmanager.model.entity.Team;
import com.interstellar.equipmentmanager.model.entity.User;
import com.interstellar.equipmentmanager.model.enums.UserRole;
import com.interstellar.equipmentmanager.repository.TeamRepository;
import com.interstellar.equipmentmanager.security.service.UserAuthorizationService;
import com.interstellar.equipmentmanager.service.TeamService;
import com.interstellar.equipmentmanager.service.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
            throw new ResourceConflictException(String.format("User with id %s cannot be add into team with id %s, because he/she is already member of the team.", userId, teamId));
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
            throw new ResourceNotFoundException(String.format("User with id %s cannot be removed from team with id %s, because he/she is not member of the team.", userId, teamId));
        }
        team.getMembers().remove(user);
        user.getTeams().remove(team);
        return mapper.map(teamRepository.save(team), TeamDTO.class);
    }
    
    @Override
    public Team getTeamById(UUID teamId) {
        return teamRepository.findById(teamId).orElseThrow(() -> new ResourceNotFoundException(Team.class.getName(), "id", teamId.toString()));
    }
    
    @Override
    public TeamMembersSizeDTO findTeamById(UUID id) {
        Team team = getTeamById(id);
        return mapper.map(team, TeamMembersSizeDTO.class);
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
    
    @Override
    public Page<UserDTO> findFilteredTeamMembersById(UUID id, String search, Pageable pageable) {
        if (!teamRepository.existsById(id)) {
            throw new ResourceNotFoundException(Team.class.getName());
        }
        
        if (search == null) {
            search = "";
        }
        
        Page<User> userPage = teamRepository.searchMembersByTeamId(id, search.toLowerCase(), pageable);
        List<UserDTO> userDTOs = userPage.get().map(u -> mapper.map(u, UserDTO.class)).toList();
        
        return new PageImpl<>(userDTOs, pageable, userDTOs.size());
    }
}
