package com.interstellar.equipmentmanager.service.impl;

import com.interstellar.equipmentmanager.exception.KeycloakUserNotFoundException;
import com.interstellar.equipmentmanager.exception.ResourceConflictException;
import com.interstellar.equipmentmanager.exception.ResourceNotFoundException;
import com.interstellar.equipmentmanager.model.dto.keycloak.user.out.KeycloakUserDTO;
import com.interstellar.equipmentmanager.model.dto.keycloak.user.in.KeycloakUserEditDTO;
import com.interstellar.equipmentmanager.model.enums.UserRole;
import com.interstellar.equipmentmanager.service.KeycloakService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeycloakServiceImpl implements KeycloakService {
    private final Keycloak keycloak;
    private final ModelMapper mapper;
    @Value("${keycloak.realm}")
    private String realm;
    @Value("${keycloak.resource}")
    private String frontendClient;

    /**
     * Searches all keycloak users by username
     *
     * @param username
     * @return list of users Important: method does not fetch users roles
     */
    @Override
    public List<KeycloakUserDTO> findAllUsers(@Nullable String username, @Nullable Pageable pageable) {
        if (pageable == null) pageable = Pageable.ofSize(Integer.MAX_VALUE);

        return keycloak.realm(realm).users()
                .search(username, null, null, null,
                        pageable.getPageSize(),
                        pageable.getPageSize(), true, false, false)
                .stream()
                .map(ur -> mapper.map(ur, KeycloakUserDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public KeycloakUserDTO getUserByPersonalNumber(@NonNull String personalNumber, boolean fetchRoles) {
        var user = getUserRepresentation(String.format("employeeId:%s", personalNumber));
        var keycloakUser = mapper.map(user, KeycloakUserDTO.class);

        if (fetchRoles) {
            var roles = getUserRoles(user, frontendClient);
            keycloakUser.setUserRoles(representationToUserRoles(roles));
        }

        return keycloakUser;
    }

    @Override
    public KeycloakUserDTO getUserByEmail(@NonNull String email, boolean fetchRoles) {
        var user = getUserRepresentation(String.format("email:%s", email));
        var keycloakUser = mapper.map(user, KeycloakUserDTO.class);

        if (fetchRoles) {
            var roles = getUserRoles(user, frontendClient);
            keycloakUser.setUserRoles(representationToUserRoles(roles));
        }

        return keycloakUser;
    }

    /**
     * Updates LDAP user role (application supports only one role).
     * Important: does not affect application database.
     *
     * @param ldapId
     * @param editDTO
     */
    @Override
    public void updateUserInKeycloak(@NonNull UUID ldapId, @NonNull KeycloakUserEditDTO editDTO) {
        UserRepresentation user = getUserRepresentation(ldapId);

        UserResource userResource = keycloak.realm(realm).users().get(user.getId());
        ClientRepresentation client = getClientRepresentation(frontendClient);

        var clientRoles = keycloak.realm(realm)
                .clients()
                .get(client.getId())
                .roles().list();

        var editRoles = editDTO
                .getUserRoles()
                .stream().filter(r -> r != UserRole.GUEST)
                .toList();

        var newRoles = clientRoles.stream()
                .filter(Objects::nonNull)
                .filter(rp -> editRoles.stream().anyMatch(r -> {
                    var a = rp.getName().toUpperCase();
                    var b = r == null ? null : r.toString().toUpperCase();
                    return a.equals(b);
                })).collect(Collectors.toList());

        var currentUserRoles = userResource
                .roles().clientLevel(client.getId()).listAll();


        if (newRoles.size() != editRoles.size()) {
            throw new ResourceNotFoundException("Could not sync users as user contains roles which are not in security system!");
        }

        userResource.roles().clientLevel(client.getId()).remove(currentUserRoles);
        userResource.roles().clientLevel(client.getId()).add(newRoles);

        log.info("Role of user with ldap id [{}] changed to '{}'.", ldapId, editRoles.toString());
    }

    @Override
    public KeycloakUserDTO getUserByLdapId(@NonNull UUID ldapid, boolean fetchRoles) throws KeycloakUserNotFoundException {

        UserRepresentation user = getUserRepresentation(ldapid);
        KeycloakUserDTO keycloakUser = mapper.map(user, KeycloakUserDTO.class);

        if (fetchRoles) {
            var roles = getUserRoles(user, frontendClient);
            keycloakUser.setUserRoles(representationToUserRoles(roles));
        }

        return keycloakUser;
    }

    private UserRepresentation getUserRepresentation(@NonNull UUID ldapId) {
        String searchAttribute = String.format("LDAP_ID:%s", ldapId.toString());
        return getUserRepresentation(searchAttribute);
    }

    private UserRepresentation getUserRepresentation(@NonNull String searchAttribute) {
        List<UserRepresentation> users = keycloak.realm(realm)
                .users()
                .searchByAttributes(searchAttribute);

        if (users.isEmpty()) {
            throw new ResourceNotFoundException(String.format("Keycloak user with parameter is [%s] not found.", searchAttribute));
        }
        if (users.size() > 1) {
            log.warn("More than one user with parameter are [{}] found.", searchAttribute);
            throw new ResourceConflictException(String.format("More than one user with parameter are [%s] found.", searchAttribute));
        }

        return users.get(0);
    }

    private ClientRepresentation getClientRepresentation(@NonNull String clientId) {
        List<ClientRepresentation> clients = keycloak.realm(realm).clients().findByClientId(clientId);

        if (clients.isEmpty()) {
            throw new IllegalStateException(String.format("Client '%s' not found.", clientId));
        }

        return clients.get(0);
    }

    private List<RoleRepresentation> getUserRoles(@NonNull UserRepresentation user, @NonNull String clientId) {
        return keycloak.realm(realm).users()
                .get(user.getId()).roles()
                .clientLevel(getClientRepresentation(clientId).getId())
                .listAll();
    }

    private List<UserRole> representationToUserRoles(@NonNull List<RoleRepresentation> roles) {
        var userRoles = new ArrayList<UserRole>();

        for (var i : roles) {
            try {
                userRoles.add(UserRole.valueOf(i.getName()));
            } catch (IllegalArgumentException e) {
                log.warn("App does not have any info about role {}", i.getName());
            }
        }
        return userRoles;
    }
}
