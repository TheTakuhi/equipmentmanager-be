package com.interstellar.equipmentmanager.security.service.imp;

import com.interstellar.equipmentmanager.exception.ResourceConflictException;
import com.interstellar.equipmentmanager.exception.ResourceNotFoundException;
import com.interstellar.equipmentmanager.model.dto.KeycloakUserDTO.KeycloakUserDTO;
import com.interstellar.equipmentmanager.model.dto.UserDTO.UserCroppedDTO;
import com.interstellar.equipmentmanager.model.dto.UserDTO.UserDTO;
import com.interstellar.equipmentmanager.model.dto.UserDTO.UserEditDTO;
import com.interstellar.equipmentmanager.model.enums.UserRole;
import com.interstellar.equipmentmanager.security.service.UserAuthorizationService;
import com.interstellar.equipmentmanager.service.KeycloakService;
import com.interstellar.equipmentmanager.service.ReactiveLdapQLService;
import com.interstellar.equipmentmanager.service.UserSyncService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.representations.AccessToken;
import org.modelmapper.ModelMapper;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserAuthorizationServiceImp implements UserAuthorizationService {
    private final ModelMapper mapper;
    private final ReactiveLdapQLService reactiveLdapQLService;
    private final KeycloakService keycloakService;
    private final UserSyncService userSyncService;

    @Override
    @Nullable
    public UserCroppedDTO getCurrentUser() {
        return getCurrentUser(SecurityContextHolder.getContext().getAuthentication());
    }

    @Override
    @Nullable
    public UserCroppedDTO getCurrentUser(@NonNull Authentication authentication) {
        var claims = getUserClaims(authentication);
        if (claims == null || claims.getLdapId() == null) return null;

        var user = syncUserFromAuth(authentication);
        return mapper.map(user, UserCroppedDTO.class);
    }

    @Override
    @Nullable
    public UserDTO syncUserFromAuth(@NonNull Authentication authentication) {
        var claims = getUserClaims(authentication);


        if (claims == null) {
            throw new ResourceNotFoundException(UserEditDTO.class.getSimpleName(), "auth token", "null");
        }

        if (claims.nonCriticalInfoMissing() || claims.criticalInfoMissing()) {
            log.info("Token does not have some info, trying to recover. Critical info missing: {}, Non critical info missing: {}",
                    claims.criticalInfoMissing(),
                    claims.nonCriticalInfoMissing());

            if (claims.getEmail() != null) {
                var ldapUsers = reactiveLdapQLService.findUserByEmail(claims.getEmail()).block();

                if (ldapUsers != null && ldapUsers.size() == 1) {
                    var newClaims = mapper.map(ldapUsers.get(0), UserEditDTO.class);
                    mapper.map(claims, newClaims);
                    claims = newClaims;
                } else {
                    log.error("Not possible to get more info from ldapQL on user email {}", claims.getEmail());
                }
            }
        }

        KeycloakUserDTO keycloakUser = null;
        if (claims.criticalInfoMissing()) {
            log.info("User [email: {}, login: {}] critical info is missing ...", claims.getEmail(), claims.getLogin());
            if (claims.getLdapId() != null)
                keycloakUser = keycloakService.getUserByLdapId(claims.getLdapId(), true);
        } else keycloakUser = mapper.map(claims, KeycloakUserDTO.class);

        if (keycloakUser == null) {
            throw new ResourceConflictException("Could not initialize keycloak user for syncing...");
        }

        return userSyncService.syncUserFromKeycloak(keycloakUser);
    }

    /**
     * function uses token for claims, so it is fast enough
     *
     * @param authentication
     * @return user claims of specified authentication
     */
    @Override
    public UserEditDTO getUserClaims(@NonNull Authentication authentication) {
        KeycloakPrincipal<?> principal = (KeycloakPrincipal<?>) authentication.getPrincipal();

        AccessToken accessToken = principal.getKeycloakSecurityContext().getToken();

        Map<String, Object> claims = accessToken.getOtherClaims();

        String ldapId = (String) claims.get("LDAP_ID");
        String employeeId = (String) claims.get("employeeId");
        String photo = (String) claims.get("photo");


        return UserEditDTO.builder()
                .ldapId(UUID.fromString(ldapId))
                .photo(photo)
                .login(accessToken.getPreferredUsername())
                .email(accessToken.getEmail())
                .firstName(accessToken.getGivenName())
                .lastName(accessToken.getFamilyName())
                .userRoles(getUserRoles(authentication))
                .build();
    }

    /**
     * @param role - user role
     * @return whether current authenticated has higher role
     */
    @Override
    public @NonNull Boolean hasMinimalRole(@Nullable String role) {
        var userRole = role == null ? UserRole.GUEST : UserRole.valueOf(role);
        return hasMinimalRole(userRole);
    }

    /**
     * @param role - user role
     * @return whether current authenticated has higher role
     */
    @Override
    public @NonNull Boolean hasMinimalRole(@NonNull UserRole role) {
        var user = getCurrentUser();
        if (user == null) return false;

        return user.getUserRoles().stream().anyMatch((r) -> r.getPower() >= role.getPower());
    }

    /**
     * @param id - id of auth user (in the majority of cases equal to ldap id, but it is not guaranteed)
     * @return whether current authenticated user id is the same
     */
    @Override
    public @NonNull Boolean hasId(@Nullable UUID id) {
        var user = getCurrentUser();
        if (user == null) return false;
        return user.getId().equals(id);
    }

    @Override
    public @NonNull Boolean hasLdapID(@Nullable UUID ldapId) {
        var claims = getUserClaims(SecurityContextHolder.getContext().getAuthentication());
        if (claims == null) return false;
        return claims.getLdapId().equals(ldapId);
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
