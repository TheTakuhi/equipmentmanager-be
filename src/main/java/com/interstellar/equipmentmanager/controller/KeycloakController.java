package com.interstellar.equipmentmanager.controller;

import com.interstellar.equipmentmanager.annotation.AlphaString;
import com.interstellar.equipmentmanager.model.dto.keycloak.user.out.KeycloakUserDTO;
import com.interstellar.equipmentmanager.model.dto.keycloak.user.in.KeycloakUserEditDTO;
import com.interstellar.equipmentmanager.model.dto.user.out.UserDTO;
import com.interstellar.equipmentmanager.security.service.UserAuthorizationService;
import com.interstellar.equipmentmanager.service.KeycloakService;
import com.interstellar.equipmentmanager.service.UserService;
import com.interstellar.equipmentmanager.service.UserSyncService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/keycloak")
@RequiredArgsConstructor
@Tag(name = "Keycloak and sync", description = "endpoints for keycloak communication")
@SecurityRequirement(name = "keycloak")
@Validated
public class KeycloakController {
    private final KeycloakService keycloakService;
    private final UserSyncService userSyncService;
    private final UserAuthorizationService userAuthorizationService;
    private final UserService userService;

    @Operation(summary = "Find users from keycloak by username/login", responses = {
            @ApiResponse(
                    description = "Found users successful",
                    responseCode = "200",
                    useReturnTypeSchema = true
            ),
            @ApiResponse(
                    description = "User is not logged in",
                    responseCode = "401",
                    content = @Content
            ),
            @ApiResponse(
                    description = "User is not authorized to do this",
                    responseCode = "403",
                    content = @Content
            )
    }, description = "Only MANAGER and ADMIN can do this (this operation takes a long time)")
    @GetMapping("/search")
    @PreAuthorize("@userAuthorizationServiceImpl.hasMinimalRole('MANAGER')")
    public List<KeycloakUserDTO> findUsersFromKeycloak(
            @Parameter(name = "login", description = "Login to search by.", required = true)
            @RequestParam(name = "login") @Valid @NotNull @AlphaString String username,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return keycloakService.findAllUsers(username, pageable);
    }

    @Operation(summary = "Get user by his ldapId from keycloak with roles", responses = {
            @ApiResponse(
                    description = "Get user successful",
                    responseCode = "200",
                    useReturnTypeSchema = true
            ),
            @ApiResponse(
                    description = "User is not logged in",
                    responseCode = "401",
                    content = @Content
            ),
            @ApiResponse(
                    description = "User is not authorized to do this",
                    responseCode = "403",
                    content = @Content
            ),
            @ApiResponse(
                    description = "User is not found",
                    responseCode = "404",
                    content = @Content
            )
    }, description = "Only MANAGER, ADMIN WITH REQUESTED LDAPID can do this (this operation takes a long time)")
    @GetMapping("/full/{ldapId}")
    @PreAuthorize("@userAuthorizationServiceImpl.hasMinimalRole('MANAGER') || " +
            "@userAuthorizationServiceImpl.hasLdapID(#ldapId)")
    public KeycloakUserDTO getKeycloakUser(@PathVariable @Valid @NotNull UUID ldapId) {
        return keycloakService.getUserByLdapId(ldapId, true);
    }

    @Operation(summary = "Sync user from keycloak by his ldap id", responses = {
            @ApiResponse(
                    description = "User was successfully updated/synced with our system",
                    responseCode = "200",
                    useReturnTypeSchema = true
            ),
            @ApiResponse(
                    description = "User is not logged in",
                    responseCode = "401",
                    content = @Content
            ),
            @ApiResponse(
                    description = "User is not authorized to do this",
                    responseCode = "403",
                    content = @Content
            ),
            @ApiResponse(
                    description = "User is not found",
                    responseCode = "404",
                    content = @Content
            )
    }, description = "Only CURRENT USER WITH THE SAME LDAPID and user with role MANAGER or higher can do this")
    @PutMapping("/sync/{ldapId}")
    @PreAuthorize("@userAuthorizationServiceImpl.hasMinimalRole('MANAGER') || " +
            "@userAuthorizationServiceImpl.hasLdapID(#ldapId)")
    public UserDTO syncUserFromKeycloak(
            @PathVariable @Valid @NotNull UUID ldapId
    ) {
        var keycloakUser = keycloakService.getUserByLdapId(ldapId, true);
        return userSyncService.syncUserFromKeycloak(keycloakUser);
    }

    @Operation(summary = "Update user in keycloak by his ldpaId", responses = {
            @ApiResponse(
                    description = "User was successfully updated/synced with our system",
                    responseCode = "200",
                    useReturnTypeSchema = true
            ),
            @ApiResponse(
                    description = "User is not logged in",
                    responseCode = "401",
                    content = @Content
            ),
            @ApiResponse(
                    description = "User is not authorized to do this",
                    responseCode = "403",
                    content = @Content
            ),
            @ApiResponse(
                    description = "User is not found",
                    responseCode = "404",
                    content = @Content
            )
    }, description = "Only CURRENT USER WITH SAME LDAPID and user with role MANAGER or higher can do this")
    @PutMapping("/update/{ldapId}")
    @PreAuthorize("@userAuthorizationServiceImpl.hasMinimalRole('MANAGER') || " +
            "@userAuthorizationServiceImpl.hasLdapID(#ldapId)")
    public KeycloakUserDTO syncUserToKeycloak(
            @PathVariable @Valid @NotNull UUID ldapId
    ) {
        var user = userService.getUserByLdapId(ldapId);
        keycloakService.updateUserInKeycloak(ldapId, new KeycloakUserEditDTO(user.getUserRoles()));
        return getKeycloakUser(ldapId);
    }

    @Operation(summary = "Syncs current user with keycloak server", responses = {
            @ApiResponse(
                    description = "Returns synced user",
                    responseCode = "200",
                    useReturnTypeSchema = true
            ),
            @ApiResponse(
                    description = "User is not logged in",
                    responseCode = "401",
                    content = @Content
            )
    })
    @PutMapping("/sync/current")
    public UserDTO syncCurrentUser() {
        return userAuthorizationService.syncUserFromAuth(SecurityContextHolder.getContext().getAuthentication());
    }
}
