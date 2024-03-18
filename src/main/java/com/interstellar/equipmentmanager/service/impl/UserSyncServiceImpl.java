package com.interstellar.equipmentmanager.service.impl;

import com.interstellar.equipmentmanager.exception.ResourceNotFoundException;
import com.interstellar.equipmentmanager.model.dto.response.LdapUser;
import com.interstellar.equipmentmanager.model.entity.Manager;
import com.interstellar.equipmentmanager.model.enums.UserRole;
import com.interstellar.equipmentmanager.model.entity.User;
import com.interstellar.equipmentmanager.repository.ManagerRepository;
import com.interstellar.equipmentmanager.repository.UserRepository;
import com.interstellar.equipmentmanager.service.KeycloakService;
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
    private final ManagerRepository managerRepository;
    private final UserRepository userRepository;
    private final ReactiveLdapQLService reactiveLdapQLService;
    private final KeycloakService keycloakService;
    private final ModelMapper mapper;
    
    @Override
    public void syncAllUsersFromLdapQL() {
        log.info("Syncing managers");
        syncAllManagers();
        
        log.info("Syncing users");
        List<LdapUser> ldapUsers = reactiveLdapQLService.findAllUsers().block();
        HashMap<UUID, List<UserRole>> usersRoles = new HashMap<>();
        
        keycloakService.getUsersByRoles(UserRole.ADMIN, UserRole.MANAGER).forEach(user -> usersRoles.put(user.getLdapId(), user.getUserRoles()));
        
        if (ldapUsers != null) {
            List<User> users = ldapUsers.stream()
                    .map(this::obtainUserWithUpdatedInfo)
                    .peek(u -> {
                        if (usersRoles.containsKey(u.getId())) {
                            u.setUserRoles(usersRoles.get(u.getId()));
                        }
                    })
                    .toList();
            
            userRepository.saveAll(users);
        }
        
        log.info("Successfully synced");
    }
    
    private User obtainUserWithUpdatedInfo(LdapUser ldapUser) {
        Optional<User> optionalUser = userRepository.findById(ldapUser.getId());
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
    private void syncAllManagers() {
        List<Manager> managers = reactiveLdapQLService.findAllManagers().block();
        
        if (managers == null) {
            throw new ResourceNotFoundException("Managers are not found");
        }
        
        managerRepository.saveAll(managers);
    }
}
