package com.interstellar.equipmentmanager.controller;

import com.interstellar.equipmentmanager.annotation.AlphaString;
import com.interstellar.equipmentmanager.exception.ResourceNotFoundException;
import com.interstellar.equipmentmanager.model.dto.CustomPageDTO;
import com.interstellar.equipmentmanager.model.dto.user.in.UserCreateDTO;
import com.interstellar.equipmentmanager.model.dto.user.out.UserCroppedDTO;
import com.interstellar.equipmentmanager.model.dto.user.out.UserDTO;
import com.interstellar.equipmentmanager.model.dto.user.in.UserEditDTO;
import com.interstellar.equipmentmanager.model.entity.User;
import com.interstellar.equipmentmanager.model.enums.UserRole;
import com.interstellar.equipmentmanager.model.filter.UserFilter;
import com.interstellar.equipmentmanager.security.service.UserAuthorizationService;
import com.interstellar.equipmentmanager.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Endpoints for managing users")
@SecurityRequirement(name = "keycloak")
@Validated
@Slf4j
public class UserController {
    private final UserService userService;
    private final UserAuthorizationService userAuthorizationService;
//    private final MailService mailService;

    @Operation(summary = "Create new user", responses = {
            @ApiResponse(
                    description = "Create users successful",
                    responseCode = "201",
                    useReturnTypeSchema = true
            ),
            @ApiResponse(
                    description = "User is not authorized to do this operation",
                    responseCode = "403",
                    content = @Content
            ),
            @ApiResponse(
                    description = "User with this login already exists",
                    responseCode = "409",
                    content = @Content
            )
    }, description = "Create user out of LDAP system (not recommended)")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @PreAuthorize("@userAuthorizationServiceImpl.hasMinimalRole('ADMIN')")
    public UserDTO createUser(@Valid @RequestBody UserCreateDTO userCreateDTO) {
        UserDTO user = userService.createUser(userCreateDTO);
//        mailService.sendUserCreateMail(userCreateDTO);
        return user;
    }

    @Operation(summary = "Get current user from security token ldap id", responses = {
            @ApiResponse(
                    description = "Return current user successful",
                    responseCode = "200",
                    useReturnTypeSchema = true
            ),
            @ApiResponse(
                    description = "User is not logged in",
                    responseCode = "401",
                    content = @Content
            ),
    }, description = "Entity is not synced automatically with keycloak")
    @GetMapping("/current")
    public UserDTO getCurrentUser() {
        var currentUser = userAuthorizationService.getCurrentUser();
        if (currentUser == null)
            throw new ResourceNotFoundException(
                    User.class.getSimpleName(),
                    "ldapId", null);

        return userService.getUserById(currentUser.getId());
    }

    @Operation(summary = "Get user by his uuid", responses = {
            @ApiResponse(
                    description = "Get user successful",
                    responseCode = "200",
                    useReturnTypeSchema = true
            ),
            @ApiResponse(
                    description = "User is not authorized to do this operation",
                    responseCode = "403",
                    content = @Content
            ),
            @ApiResponse(
                    description = "User is not found",
                    responseCode = "404",
                    content = @Content
            )
    }, description = "Only CURRENT USER can see info about him")
    @GetMapping("/{id}")
    @PreAuthorize("@userAuthorizationServiceImpl.hasMinimalRole('ADMIN') ||"
            + " @userAuthorizationServiceImpl.hasId(#id)")
    public UserDTO getUser(@PathVariable UUID id) {
        return userService.getUserById(id);
    }

    @Operation(summary = "Get users by filter", responses = {
            @ApiResponse(
                    description = "Get users successful",
                    responseCode = "200",
                    useReturnTypeSchema = true
            ),
            @ApiResponse(
                    description = "User is not authorized to do this operation",
                    responseCode = "403",
                    content = @Content
            )
    }, description = "Only MANAGER and ADMIN can see all users")
    @GetMapping
    @PreAuthorize("@userAuthorizationServiceImpl.hasMinimalRole('MANAGER')")
    public CustomPageDTO<UserCroppedDTO> getUsers(
            @Parameter(description = "Search by user roles", array = @ArraySchema(schema = @Schema(implementation = UserRole.class)))
            @RequestParam(required = false) List<UserRole> userRoles,
            @Parameter(description = "Search by user login", schema = @Schema(implementation = String.class))
            @RequestParam(required = false) String login,
            @Parameter(description = "Search by user fullname", schema = @Schema(implementation = String.class))
            @RequestParam(required = false) @AlphaString String fullName,
            @Parameter(description = "Include removed users", schema = @Schema(implementation = Boolean.class, defaultValue = "false"))
            @RequestParam(required = false, defaultValue = "false") boolean includeRemoved,
            @SortDefault(sort = "login", direction = Sort.Direction.DESC) @PageableDefault(size = 50) Pageable pageable) {
        var filter = new UserFilter(login, fullName, userRoles, includeRemoved);
        var standardPage = userService.getAllUsers(filter, pageable);
        return new CustomPageDTO<>(
                standardPage.getTotalElements(),
                standardPage.getTotalPages(),
                standardPage.getContent().size(),
                standardPage.getContent(),
                pageable,
                standardPage.getTotalPages() > pageable.getPageNumber() + 1,
                pageable.getPageNumber() > 0
        );
    }

    @Operation(summary = "Update user info (only role and states can be changed)", responses = {
            @ApiResponse(
                    description = "Update of user is successful",
                    responseCode = "200",
                    useReturnTypeSchema = true
            ),
            @ApiResponse(
                    description = "User is not authorized to do this operation",
                    responseCode = "403",
                    content = @Content
            ),
            @ApiResponse(
                    description = "User is not found",
                    responseCode = "404",
                    content = @Content
            )
    }, description = "Other properties should be updated from ldap server or using another service (current user cant change his role but can change preferred settings)")
    @PutMapping("/{id}")
    @PreAuthorize("(@userAuthorizationServiceImpl.hasId(#id) && #userEditDTO.userRoles == null) "
            + "|| @userAuthorizationServiceImpl.hasMinimalRole('ADMIN')")
    public UserDTO updateUser(
            @PathVariable UUID id,
            @Valid @RequestBody UserEditDTO userEditDTO,
            @Parameter(description = "Keycloak flag for auto sync", schema = @Schema(implementation = Boolean.class, defaultValue = "false"))
            @RequestParam(defaultValue = "false") Boolean syncRolesToKeycloak) {
        UserDTO user = userService.updateUser(id, userEditDTO, syncRolesToKeycloak);
//        if (userEditDTO.getUserRoles() != null) {
//            mailService.sendUserUpdateMail(id);
//        }
        return user;
    }

    @Operation(summary = "Delete user from local database", responses = {
            @ApiResponse(
                    description = "Delete of user is successful",
                    responseCode = "204",
                    useReturnTypeSchema = true
            ),
            @ApiResponse(
                    description = "User is not authorized to do this operation",
                    responseCode = "403",
                    content = @Content
            ),
            @ApiResponse(
                    description = "User is not found",
                    responseCode = "404",
                    content = @Content
            )
    }, description = "Only ADMIN can delete user")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @PreAuthorize("@userAuthorizationServiceImpl.hasMinimalRole('ADMIN')")
    public void deleteUser(@PathVariable UUID id) {
        userService.deleteUserById(id);
    }

    @Operation(summary = "Available roles search", responses = {
            @ApiResponse(
                    description = "Roles get is successful",
                    responseCode = "200",
                    useReturnTypeSchema = true
            )
    }, description = "ANYBODY can see available roles")
    @GetMapping("/roles")
    public List<UserRole> getAvailableRoles() {
        return Arrays.stream(UserRole.values()).collect(Collectors.toList());
    }
}
