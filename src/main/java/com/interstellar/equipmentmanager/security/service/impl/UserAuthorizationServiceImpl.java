package com.interstellar.equipmentmanager.security.service.impl;

import com.interstellar.equipmentmanager.exception.ResourceConflictException;
import com.interstellar.equipmentmanager.exception.ResourceNotFoundException;
import com.interstellar.equipmentmanager.model.dto.keycloak.user.out.KeycloakUserDTO;
import com.interstellar.equipmentmanager.model.dto.user.in.UserEditDTO;
import com.interstellar.equipmentmanager.model.dto.user.out.CurrentUserDTO;
import com.interstellar.equipmentmanager.model.dto.user.out.UserCroppedDTO;
import com.interstellar.equipmentmanager.model.dto.user.out.UserDTO;
import com.interstellar.equipmentmanager.model.enums.UserRole;
import com.interstellar.equipmentmanager.security.service.UserAuthorizationService;
import com.interstellar.equipmentmanager.service.KeycloakService;
import com.interstellar.equipmentmanager.service.ReactiveLdapQLService;
import com.interstellar.equipmentmanager.service.UserService;
import com.interstellar.equipmentmanager.service.UserSyncService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserAuthorizationServiceImpl implements UserAuthorizationService {
    private final ModelMapper mapper;
    private final UserService userService;

    @Value("${keycloak.resource}")
    private String keycloakResource;

    @Override
    @Nullable
    public UserCroppedDTO getCurrentUserCropped() {
        return mapper.map(getCurrentUser(SecurityContextHolder.getContext().getAuthentication()), UserCroppedDTO.class);
    }

    @Override
    @Nullable
    public UserDTO getCurrentUser() {
        return getCurrentUser(SecurityContextHolder.getContext().getAuthentication());
    }

    @Override
    @Nullable
    public UserDTO getCurrentUser(@NonNull Authentication authentication) {
        var claims = getUserClaims(authentication);
        if (claims == null || claims.getId() == null) return null;

        return userService.getUserById(claims.getId());
    }

    /**
     * function uses token for claims, so it is fast enough
     *
     * @param authentication
     * @return user claims of specified authentication
     */
    @Override
    public CurrentUserDTO getUserClaims(@NonNull Authentication authentication) {
        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) authentication;
            Map<String, Object> claims = jwtToken.getTokenAttributes();
            Map<String, Object> resourceAccess = (Map<String, Object>) jwtToken.getTokenAttributes().get("resource_access");

            List<String> roles = Collections.emptyList();
            if (resourceAccess != null) {
                Map<String, Object> clientRoles = (Map<String, Object>) resourceAccess.get(keycloakResource);

                if (clientRoles != null) {
                    // Extract roles from clientRoles and return
                    roles = (List<String>) clientRoles.get("roles");
                }
            }

            String ldapId = (String) claims.get("LDAP_ID");
            String photo = (String) claims.get("photo");

            return CurrentUserDTO.builder()
                    .id(UUID.fromString(ldapId))
                    .photo(photo)
                    .login((String) claims.get("preferred_username"))
                    .email((String) claims.get("email"))
                    .firstName((String) claims.get("given_name"))
                    .lastName((String) claims.get("family_name"))
                    .userRoles(roles.stream()
                            .map(g -> {
                                try {
                                    return UserRole.valueOf(g);
                                } catch (IllegalArgumentException e) {
                                    return null;
                                }
                            })
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList()))
                    .build();
        } else {
            throw new IllegalArgumentException("Unsupported authentication token type");
        }
    }

    /**
     * @param role - user role
     * @return whether current authenticated has higher role
     */
    @Override
    public @NonNull Boolean hasMinimalRole(@Nullable String role) {
        var userRole = role == null ? UserRole.GUEST : UserRole.valueOf(role);
        System.out.println(role);
        return hasMinimalRole(userRole);
    }

    /**
     * @param role - user role
     * @return whether current authenticated has higher role
     */
    @Override
    public @NonNull Boolean hasMinimalRole(@NonNull UserRole role) {
        var user = getCurrentUserCropped();
        if (user == null) return false;

        return user.getUserRoles().stream().anyMatch((r) -> r.getPower() >= role.getPower());
    }

    /**
     * @param id - id of auth user (in the majority of cases equal to ldap id, but it is not guaranteed)
     * @return whether current authenticated user id is the same
     */
    @Override
    public @NonNull Boolean hasId(@Nullable UUID id) {
        var user = getCurrentUserCropped();
        if (user == null) return false;
        return user.getId().equals(id);
    }

    @Override
    public @NonNull Boolean hasLdapID(@Nullable UUID ldapId) {
        var claims = getUserClaims(SecurityContextHolder.getContext().getAuthentication());
        if (claims == null) return false;
        return claims.getId().equals(ldapId);
    }

    private List<UserRole> getUserRoles(Authentication authentication) {
        return authentication
                .getAuthorities()
                .stream()
                .filter(g -> g.getAuthority().contains("ROLE_") && g.getAuthority().length() > 5)
                .map(g -> {
                    try {
                        return UserRole.valueOf(g.getAuthority().substring(5));
                    } catch (IllegalArgumentException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
