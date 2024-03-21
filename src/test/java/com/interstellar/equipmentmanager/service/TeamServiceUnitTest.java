package com.interstellar.equipmentmanager.service;

import com.interstellar.equipmentmanager.model.dto.team.in.TeamCreateDTO;
import com.interstellar.equipmentmanager.model.dto.team.out.TeamDTO;
import com.interstellar.equipmentmanager.model.dto.user.out.UserCroppedDTO;
import com.interstellar.equipmentmanager.model.dto.user.out.UserDTO;
import com.interstellar.equipmentmanager.model.entity.Team;
import com.interstellar.equipmentmanager.model.entity.User;
import com.interstellar.equipmentmanager.repository.TeamRepository;
import com.interstellar.equipmentmanager.security.service.UserAuthorizationService;
import com.interstellar.equipmentmanager.service.impl.TeamServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class TeamServiceUnitTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private ModelMapper mapper;

    @Mock
    private UserAuthorizationService userAuthorizationService;

    @Mock
    private UserService userService;

    @InjectMocks
    private TeamServiceImpl teamServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void createTeam() {
        TeamCreateDTO teamCreateDTO = new TeamCreateDTO();
        teamCreateDTO.setTeamName("TeamName");
        ArrayList<UUID> teamNames = new ArrayList<>();
        teamNames.add(new UUID(123, 123));
        teamCreateDTO.setMembersIds(teamNames);
        teamCreateDTO.setOwnerId(new UUID(123, 123));
        UserCroppedDTO currentUserCropped = new UserCroppedDTO();

        UserDTO currentUserDTO = new UserDTO();
        currentUserDTO.setId(currentUserCropped.getId());

        when(userAuthorizationService.getCurrentUser()).thenReturn(currentUserDTO);
        when(mapper.map(teamCreateDTO, Team.class)).thenReturn(new Team());
        when(mapper.map(currentUserCropped, User.class)).thenReturn(new User());

        //??????????????????????????????
        when(userService.getOriginalUser(any())).thenReturn(new User());

        when(teamRepository.save(any(Team.class))).thenAnswer(invocation -> {
            Team savedTeam = invocation.getArgument(0);
            savedTeam.setId(UUID.randomUUID());
            return savedTeam;
        });

        TeamDTO teamDTO = new TeamDTO();
        when(mapper.map(any(Team.class), eq(TeamDTO.class))).thenReturn(teamDTO);

        TeamDTO result = teamServiceImpl.createTeam(teamCreateDTO);
        assertNotNull(result);
    }


    @Test
    void updateTeam() {
    }

    @Test
    void addUserToTeam() {
    }

    @Test
    void removeUserFromTeam() {
    }

    @Test
    void getTeamById() {
    }

    @Test
    void getAllTeams() {
    }

    @Test
    void isOwner() {
    }

    @Test
    void deleteTeamById() {
    }
}