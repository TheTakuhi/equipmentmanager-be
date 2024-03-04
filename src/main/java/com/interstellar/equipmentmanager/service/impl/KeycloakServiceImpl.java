package com.interstellar.equipmentmanager.service.impl;

import com.interstellar.equipmentmanager.exception.ResourceConflictException;
import com.interstellar.equipmentmanager.exception.ResourceNotFoundException;
import com.interstellar.equipmentmanager.model.dto.keycloak.user.out.KeycloakUserDTO;
import com.interstellar.equipmentmanager.model.dto.keycloak.user.in.KeycloakUserEditDTO;
import com.interstellar.equipmentmanager.model.enums.UserRole;
import com.interstellar.equipmentmanager.service.KeycloakService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public Set<KeycloakUserDTO> getUsersByRoles(UserRole... userRoles) {
        ClientRepresentation client = getClientRepresentation(frontendClient);
        ClientResource clientResource = keycloak.realm(realm).clients().get(client.getId());

        return Arrays.stream(userRoles)
                .map(role -> clientResource.roles().get(role.name()).getUserMembers().stream()
                        .map(userRepresentation -> {
                            KeycloakUserDTO keycloakUserDTO = new KeycloakUserDTO();
                            keycloakUserDTO.setLdapId(UUID.fromString(userRepresentation.firstAttribute("LDAP_ID")));

                            List<UserRole> roles = representationToUserRoles(getUserRoles(userRepresentation, client.getClientId()));
                            keycloakUserDTO.setUserRoles(roles);

                            return keycloakUserDTO;
                        })
                        .collect(Collectors.toSet())
                )
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }

    @Override
    public KeycloakUserDTO getUserByLdapId(@NonNull UUID ldapid, boolean fetchRoles) {

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

    @Override
    public boolean existsUserInKeycloak(UUID ldapId) {
        String searchAttribute = String.format("LDAP_ID:%s", ldapId.toString());
        List<UserRepresentation> users = keycloak.realm(realm)
                .users()
                .searchByAttributes(searchAttribute);

        if (users.size() > 1) {
            log.warn("More than one user with parameter are [{}] found.", searchAttribute);
            throw new ResourceConflictException(String.format("More than one user with parameter are [%s] found.", searchAttribute));
        }

        return users.size() == 1;
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

    @Override
    public List<UserRole> getUserRolesByLdapId(UUID ldapId) {
        UserRepresentation userRepresentation = getUserRepresentation(ldapId);
        List<RoleRepresentation> roleRepresentations = getUserRoles(userRepresentation, frontendClient);
        return representationToUserRoles(roleRepresentations);
    }
}
