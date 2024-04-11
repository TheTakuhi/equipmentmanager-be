package com.interstellar.equipmentmanager.service;

import com.interstellar.equipmentmanager.exception.ResourceConflictException;
import com.interstellar.equipmentmanager.exception.ResourceNotFoundException;
import com.interstellar.equipmentmanager.model.dto.team.in.TeamCreateDTO;
import com.interstellar.equipmentmanager.model.dto.team.in.TeamEditDTO;
import com.interstellar.equipmentmanager.model.dto.team.out.TeamDTO;
import com.interstellar.equipmentmanager.model.dto.team.out.TeamMembersSizeDTO;
import com.interstellar.equipmentmanager.model.dto.user.out.UserCroppedDTO;
import com.interstellar.equipmentmanager.model.dto.user.out.UserDTO;
import com.interstellar.equipmentmanager.model.entity.Team;
import com.interstellar.equipmentmanager.model.entity.User;
import com.interstellar.equipmentmanager.model.enums.UserRole;
import com.interstellar.equipmentmanager.repository.TeamRepository;
import com.interstellar.equipmentmanager.security.service.UserAuthorizationService;
import com.interstellar.equipmentmanager.service.impl.TeamServiceImpl;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

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
    void addUserToTeam_Success() {
        // Mock data
        UUID teamId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Team team = new Team();
        team.setId(teamId);

        List<User> members = new ArrayList<>();
        members.add(new User());
        team.setMembers(members);

        User user = new User();
        user.setId(userId);

        Team team2 = team;
        team2.setMembers(Stream.concat(team.getMembers().stream(), Stream.of(user)).toList());

        UserCroppedDTO userCroppedDTO = new UserCroppedDTO();
        userCroppedDTO.setId(userId);

        List<UserCroppedDTO> membersCropped = new ArrayList<>();
        membersCropped.add(new UserCroppedDTO());
        membersCropped.add(userCroppedDTO);

        TeamDTO teamDTO = new TeamDTO();
        teamDTO.setId(teamId);
        teamDTO.setMembers(membersCropped);

        // Mock repositories
        when(teamRepository.findById(teamId)).thenReturn(Optional.of(team));
        when(userService.getOriginalUser(userId)).thenReturn(user);
        when(teamRepository.save(team)).thenReturn(team2);
        when(mapper.map(team2, TeamDTO.class)).thenReturn(teamDTO);

        // Call the method
        TeamDTO result = teamServiceImpl.addUserToTeam(teamId, userId);

        // Assertions
        assertEquals(teamId, result.getId());
        assertTrue(result.getMembers().contains(userCroppedDTO));

        // Verify interactions
        verify(teamRepository).findById(teamId);
        verify(userService).getOriginalUser(userId);
        verify(teamRepository).save(team);
    }

    @Test
    void addUserToTeam_AlreadyMember() {
        // Mock data
        UUID teamId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Team team = new Team();
        team.setId(teamId);

        User user = new User();
        user.setId(userId);

        List<User> members = List.of(user);
        team.setMembers(members);

        // Mock repositories
        when(teamRepository.findById(teamId)).thenReturn(Optional.of(team));
        when(userService.getOriginalUser(userId)).thenReturn(user);

        // Call the method and assert
        ResourceConflictException exception = assertThrows(ResourceConflictException.class,
                () -> teamServiceImpl.addUserToTeam(teamId, userId));
        assertEquals(String.format("User with id %s cannot be add into team with id %s, because he/she is already member of the team.", userId, teamId), exception.getMessage());

        // Verify interactions
        verify(teamRepository).findById(teamId);
        verify(userService).getOriginalUser(userId);
    }

    @Test
    void removeUserFromTeam_Success() {
        // Mock data
        UUID teamId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Team team = new Team();
        team.setId(teamId);

        User user = new User();
        user.setId(userId);
        UserCroppedDTO userDTO = new UserCroppedDTO();
        userDTO.setId(userId);

        List<User> members = List.of(user);
        team.setMembers(members);

        // Mock repositories
        when(teamRepository.findById(teamId)).thenReturn(Optional.of(team));
        when(userService.getOriginalUser(userId)).thenReturn(user);
        when(teamRepository.save(team)).thenReturn(team);

        // Call the method
        TeamDTO result = teamServiceImpl.removeUserFromTeam(teamId, userId);

        // Assertions
        assertEquals(teamId, result.getId());
        assertFalse(result.getMembers().contains(userDTO));

        // Verify interactions
        verify(teamRepository).findById(teamId);
        verify(userService).getOriginalUser(userId);
        verify(teamRepository).save(team);
    }

    @Test
    void removeUserFromTeam_NotMember() {
        // Mock data
        UUID teamId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Team team = new Team();
        team.setId(teamId);
        User user = new User();
        user.setId(userId);
        // Mock repositories
        when(teamRepository.findById(teamId)).thenReturn(Optional.of(team));
        when(userService.getOriginalUser(userId)).thenReturn(user);

        // Call the method and assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> teamServiceImpl.removeUserFromTeam(teamId, userId));
        assertEquals(String.format("User with id %s cannot be removed from team with id %s, because he/she is not member of the team. searched could not be found.", userId, teamId), exception.getMessage());

        // Verify interactions
        verify(teamRepository).findById(teamId);
        verify(userService).getOriginalUser(userId);
    }

    @Test
    void getTeamById_Success() {
        // Mock data
        UUID teamId = UUID.randomUUID();
        Team team = new Team();
        team.setId(teamId);
        TeamDTO teamDTO = new TeamDTO();
        // Mock repository
        when(teamRepository.findById(teamId)).thenReturn(Optional.of(team));
        when(mapper.map(team, TeamDTO.class)).thenReturn(teamDTO);

        // Call the method
        TeamDTO result = mapper.map(teamServiceImpl.getTeamById(teamId), TeamDTO.class);

        // Assertions
        assertEquals(teamDTO, result);

        // Verify interactions
        verify(teamRepository).findById(teamId);
        verify(mapper).map(team, TeamDTO.class);
    }

    @Test
    void getTeamById_NotFound() {
        // Mock data
        UUID teamId = UUID.randomUUID();

        // Mock repository
        when(teamRepository.findById(teamId)).thenReturn(Optional.empty());

        // Call the method and assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> teamServiceImpl.getTeamById(teamId));
        assertEquals(String.format("com.interstellar.equipmentmanager.model.entity.Team searched by id with value: '%s' could not be found.", teamId), exception.getMessage());

        // Verify interactions
        verify(teamRepository).findById(teamId);

        // Make sure mapper.map() is not called
        verify(mapper, Mockito.never()).map(Mockito.any(), Mockito.eq(TeamDTO.class));
    }

    @Test
    void getAllTeams_UserRole_Success() {
        // Mock data
        UUID userId = UUID.randomUUID();
        UserDTO normalUser = new UserDTO();
        normalUser.setId(userId);
        normalUser.setUserRoles(List.of(UserRole.GUEST));

        List<Team> teams = Arrays.asList(new Team(), new Team());
        Pageable pageable = mock(Pageable.class);
        String search = "";
        Page<Team> teamPage = new PageImpl<>(teams);

        when(userAuthorizationService.getCurrentUser()).thenReturn(normalUser);
        when(teamRepository.findByMembersIdAndSearch(userId, search, pageable)).thenReturn(teamPage);

        // Call the method
        Page<TeamMembersSizeDTO> result = teamServiceImpl.getAllTeams(pageable, search);

        // Assertions
        assertEquals(teamPage.map(element -> mapper.map(element, TeamMembersSizeDTO.class)), result);
    }

    @Test
    void deleteTeamById_Success() {
        // Mock data
        UUID teamId = UUID.randomUUID();
        Team team = new Team();
        team.setId(teamId);
        List<User> members = new ArrayList<>();
        List<Team> teams = new ArrayList<>();
        teams.add(team);
        User member = new User();
        member.setTeams(teams);
        members.add(member);
        team.setMembers(members);

        // Mock repository
        when(teamRepository.findById(teamId)).thenReturn(Optional.of(team));

        // Call the method
        teamServiceImpl.deleteTeamById(teamId);

        // Verify interactions
        verify(teamRepository).findById(teamId);
        verify(teamRepository).deleteById(teamId);

        when(teamRepository.findById(teamId)).thenThrow(ResourceNotFoundException.class);
    }

    @Test
    void deleteTeamById_TeamNotFound() {
        // Mock data
        UUID teamId = UUID.randomUUID();

        // Mock repository
        when(teamRepository.findById(teamId)).thenReturn(Optional.empty());

        // Call the method and assert
        assertThrows(ResourceNotFoundException.class, () -> teamServiceImpl.deleteTeamById(teamId));

        // Verify interactions
        verify(teamRepository).findById(teamId);

        // Verify that deleteById is not called when team is not found
        verify(teamRepository, never()).deleteById(teamId);
    }
}