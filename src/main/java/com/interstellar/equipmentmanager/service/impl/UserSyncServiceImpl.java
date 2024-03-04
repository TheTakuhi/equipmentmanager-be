package com.interstellar.equipmentmanager.service.impl;

import com.interstellar.equipmentmanager.model.dto.response.LdapUser;
import com.interstellar.equipmentmanager.model.entity.Manager;
import com.interstellar.equipmentmanager.model.enums.AuditActionType;
import com.interstellar.equipmentmanager.model.enums.UserRole;
import com.interstellar.equipmentmanager.model.entity.AuditInfo;
import com.interstellar.equipmentmanager.model.entity.User;
import com.interstellar.equipmentmanager.repository.UserRepository;
import com.interstellar.equipmentmanager.service.ManagerService;
import com.interstellar.equipmentmanager.service.UserSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserSyncServiceImpl implements UserSyncService {
    private final UserRepository userRepository;
    private final ReactiveLdapQLServiceImpl reactiveLdapQLService;
    private final ModelMapper mapper;
    private final ManagerService managerService;

    public void syncAllUsersFromLdapQL() {
        log.info("Syncing users");
        List<LdapUser> ldapUsers = reactiveLdapQLService.findAllUsers().block();
        HashMap<UUID, Manager> managers = new HashMap<>();

        if (ldapUsers != null) {
            List<User> users = ldapUsers.stream()
                    .map(ldapUser -> {
                        Optional<User> optionalUser = userRepository.findById(ldapUser.getObjectGUID());
                        User user;

                        if (optionalUser.isPresent()) {
                            user = optionalUser.get();
                            mapper.map(ldapUser, user);
                        } else {
                            user = mapper.map(ldapUser, User.class);
                            user.setAuditInfo(new AuditInfo(
                                    Instant.now(),
                                    Instant.now(),
                                    AuditActionType.SYNC_SYSTEM,
                                    AuditActionType.SYNC_SYSTEM
                            ));
                            user.setUserRoles(List.of(UserRole.GUEST));
                        }

                        if (ldapUser.getManager() != null) {
                            user.getManager().setId(ldapUser.getManager().getObjectGUID());
                        }
                        return user;

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
}
