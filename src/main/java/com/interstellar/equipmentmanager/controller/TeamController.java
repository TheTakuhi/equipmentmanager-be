package com.interstellar.equipmentmanager.controller;

import com.interstellar.equipmentmanager.model.dto.team.in.TeamCreateDTO;
import com.interstellar.equipmentmanager.model.dto.team.in.TeamEditDTO;
import com.interstellar.equipmentmanager.model.dto.team.out.TeamDTO;
import com.interstellar.equipmentmanager.security.service.impl.UserAuthorizationServiceImpl;
import com.interstellar.equipmentmanager.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
@Tag(name = "Teams", description = "Endpoints for managing teams")
@SecurityRequirement(name = "keycloak")
@Validated
@Slf4j
public class TeamController {

    private final TeamService teamService;

    @Operation(summary = "Create new team", responses = {
            @ApiResponse(
                    description = "Create teams successful",
                    responseCode = "201",
                    useReturnTypeSchema = true
            ),
            @ApiResponse(
                    description = "User is not authorized to do this operation",
                    responseCode = "403",
                    content = @Content
            ),
            @ApiResponse(
                    description = "Team with this name already exists",
                    responseCode = "409",
                    content = @Content
            )
    }, description = "Create team with currently logged in user as owner")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @PreAuthorize("@userAuthorizationServiceImpl.hasMinimalRole('MANAGER')")
    public TeamDTO createTeam(@Valid @RequestBody TeamCreateDTO teamCreateDTO) {
        return teamService.createTeam(teamCreateDTO);
    }

    @Operation(summary = "Update team", responses = {
            @ApiResponse(
                    description = "Update of team is successful",
                    responseCode = "200",
                    useReturnTypeSchema = true
            ),
            @ApiResponse(
                    description = "User is not authorized to do this operation",
                    responseCode = "403",
                    content = @Content
            ),
            @ApiResponse(
                    description = "Team is not found",
                    responseCode = "404",
                    content = @Content
            )
    }, description = "Update team by id")
    @PutMapping("/{id}")
    @PreAuthorize("@teamServiceImpl.isOwner(#id) || @userAuthorizationServiceImpl.hasMinimalRole('ADMIN') ")
    public TeamDTO updateTeam(
            @PathVariable UUID id,
            @Valid @RequestBody TeamEditDTO teamEditDTO) {
        return teamService.updateTeam(id, teamEditDTO);
    }

    @Operation(summary = "Add user to team", responses = {
            @ApiResponse(
                    description = "Adding user to team is successful",
                    responseCode = "200",
                    useReturnTypeSchema = true
            ),
            @ApiResponse(
                    description = "User is not authorized to do this operation",
                    responseCode = "403",
                    content = @Content
            ),
            @ApiResponse(
                    description = "Team is not found",
                    responseCode = "404",
                    content = @Content
            )
    }, description = "Adding user to team by id")
    @PatchMapping("/{teamId}/add/{userId}")
    @PreAuthorize("@teamServiceImpl.isOwner(#teamId) || @userAuthorizationServiceImpl.hasMinimalRole('ADMIN') ")
    public TeamDTO addUserToTeam(
            @PathVariable("teamId") UUID teamId,
            @PathVariable("userId") UUID userId) {
        return teamService.addUserToTeam(teamId, userId);
    }

    @Operation(summary = "Remove user from team", responses = {
            @ApiResponse(
                    description = "The removal of user from team was successful",
                    responseCode = "200",
                    useReturnTypeSchema = true
            ),
            @ApiResponse(
                    description = "User is not authorized to do this operation",
                    responseCode = "403",
                    content = @Content
            ),
            @ApiResponse(
                    description = "Team is not found",
                    responseCode = "404",
                    content = @Content
            )
    }, description = "Removing user from team by id")
    @PatchMapping("/{teamId}/remove/{userId}")
    @PreAuthorize("@teamServiceImpl.isOwner(#teamId) || @userAuthorizationServiceImpl.hasMinimalRole('ADMIN') ")
    public TeamDTO removeUserFromTeam(
            @PathVariable("teamId") UUID teamId,
            @PathVariable("userId") UUID userId) {
        return teamService.removeUserFromTeam(teamId, userId);
    }


    @Operation(summary = "Get all teams", responses = {
            @ApiResponse(
                    description = "Get teams successful",
                    responseCode = "200",
                    useReturnTypeSchema = true
            ),
            @ApiResponse(
                    description = "User is not authorized to do this operation",
                    responseCode = "403",
                    content = @Content
            )
    }, description = "Get all teams")
    @GetMapping
    @PreAuthorize("@userAuthorizationServiceImpl.hasMinimalRole('MANAGER')")
    public Page<TeamDTO> getAllTeams(@PageableDefault(size = 20) Pageable pageable) {
        return teamService.getAllTeams(pageable);
    }


    @Operation(summary = "Get team by its uuid", responses = {
            @ApiResponse(
                    description = "Get team successful",
                    responseCode = "200",
                    useReturnTypeSchema = true
            ),
            @ApiResponse(
                    description = "User is not authorized to do this operation",
                    responseCode = "403",
                    content = @Content
            ),
            @ApiResponse(
                    description = "Team is not found",
                    responseCode = "404",
                    content = @Content
            )
    }, description = "Get team by its id")
    @GetMapping("/{id}")
    @PreAuthorize("@userAuthorizationServiceImpl.hasMinimalRole('MANAGER') ")
    public TeamDTO getTeam(@PathVariable UUID id) {
        return teamService.getTeamById(id);
    }

    @Operation(summary = "Delete team from local database", responses = {
            @ApiResponse(
                    description = "Delete of team is successful",
                    responseCode = "204",
                    useReturnTypeSchema = true
            ),
            @ApiResponse(
                    description = "User is not authorized to do this operation",
                    responseCode = "403",
                    content = @Content
            ),
            @ApiResponse(
                    description = "Team has not been found",
                    responseCode = "404",
                    content = @Content
            )
    }, description = "Only ADMIN and MANAGER can delete team")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @PreAuthorize("@userAuthorizationServiceImpl.hasMinimalRole('MANAGER')")
    public void deleteTeam(@PathVariable UUID id) {
        teamService.deleteTeamById(id);
    }
}
