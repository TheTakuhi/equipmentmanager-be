package com.interstellar.equipmentmanager.service.impl;

import com.interstellar.equipmentmanager.model.dto.response.LdapUser;
import com.interstellar.equipmentmanager.model.entity.Manager;
import com.interstellar.equipmentmanager.model.enums.UserRole;
import com.interstellar.equipmentmanager.model.entity.User;
import com.interstellar.equipmentmanager.repository.UserRepository;
import com.interstellar.equipmentmanager.service.KeycloakService;
import com.interstellar.equipmentmanager.service.ManagerService;
import com.interstellar.equipmentmanager.service.ReactiveLdapQLService;
import com.interstellar.equipmentmanager.service.UserSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserSyncServiceImpl implements UserSyncService {
    private final UserRepository userRepository;
    private final ReactiveLdapQLService reactiveLdapQLService;
    private final KeycloakService keycloakService;
    private final ModelMapper mapper;
    private final ManagerService managerService;

    @Override
    public void syncAllUsersFromLdapQL() {
        log.info("Syncing users");
        List<LdapUser> ldapUsers = reactiveLdapQLService.findAllUsers().block();
        HashMap<UUID, Manager> managers = new HashMap<>();
        HashMap<UUID, List<UserRole>> usersRoles = new HashMap<>();

        keycloakService.getUsersByRoles(UserRole.ADMIN, UserRole.MANAGER).forEach(user -> {
            usersRoles.put(user.getLdapId(), user.getUserRoles());
        });

        if (ldapUsers != null) {
            List<User> users = ldapUsers.stream()
                    .map(ldapUser -> {
                        User user = obtainUserWithUpdatedInfo(ldapUser);
                        return setUserManagerId(ldapUser, user);
                    })
                    .peek(u -> {
                        if (usersRoles.containsKey(u.getId())) {
                            u.setUserRoles(usersRoles.get(u.getId()));
                        }
                    })
                    .peek(u -> {
                        if (u.getManager() != null) {
                            if (!managers.containsKey(u.getManager().getId())) {
                                managers.put(u.getManager().getId(), managerService.save(u.getManager()));
                            }
                            u.setManager(managers.get(u.getManager().getId()));
                        }
                    }).toList();
            userRepository.saveAll(users);
        }

        log.info("Successfully synced");
    }

    private User setUserManagerId(LdapUser ldapUser, User user) {
        if (ldapUser.getManager() != null) {
            user.getManager().setId(ldapUser.getManager().getObjectGUID());
        }
        return user;
    }
    private User obtainUserWithUpdatedInfo(LdapUser ldapUser) {
        Optional<User> optionalUser = userRepository.findById(ldapUser.getObjectGUID());
        User user;

        if (optionalUser.isPresent()) {
            user = optionalUser.get();
            mapper.map(ldapUser, user);
        } else {
            user = mapper.map(ldapUser, User.class);
            user.setUserRoles(List.of(UserRole.GUEST));
        }
        return user;
    }
}
