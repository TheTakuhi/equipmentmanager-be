package com.interstellar.equipmentmanager.service.impl;

import com.interstellar.equipmentmanager.model.dto.request.LdapUser;
import com.interstellar.equipmentmanager.model.enums.AuditActionType;
import com.interstellar.equipmentmanager.model.enums.UserRole;
import com.interstellar.equipmentmanager.model.entity.AuditInfo;
import com.interstellar.equipmentmanager.model.entity.User;
import com.interstellar.equipmentmanager.repository.UserRepository;
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

    public void syncAllUsersFromLdapQL() {
        syncMissingUsersFromLdapQL();

        List<LdapUser> ldapUsers = reactiveLdapQLService.findAllUsers().blockFirst();
        List<User> users = userRepository.findAll();
        HashMap<UUID, LdapUser> ldapUsersMap = new HashMap<>();

        ldapUsers.forEach(lu -> ldapUsersMap.put(lu.getObjectGUID(), lu));

        List<User> toUpdateUsers = users.stream()
                .filter(u -> ldapUsersMap.get(u.getId()) != null)
                .peek(u -> mapper.map(ldapUsersMap.get(u.getId()), u))
                .peek(u -> u.setAuditInfo(new AuditInfo(
                        u.getAuditInfo().getCreatedAt(),
                        Instant.now(),
                        u.getAuditInfo().getCreatedBy(),
                        AuditActionType.SYNC_SYSTEM.name()
                )))
                .toList();

        userRepository.saveAll(toUpdateUsers);
    }

    public void syncMissingUsersFromLdapQL() {
        List<User> users = userRepository.findAll();
        List<LdapUser> ldapUsers = reactiveLdapQLService.findAllUsersNotIn(users)
                .blockOptional()
                .orElse(List.of());

        List<User> newUsers = ldapUsers.stream()
                .map(ldapUser -> mapper.map(ldapUser, User.class))
                .peek(u -> u.setUserRoles(List.of(UserRole.GUEST)))
                .peek(u -> u.setAuditInfo(new AuditInfo(
                        Instant.now(),
                        Instant.now(),
                        AuditActionType.SYNC_SYSTEM.name(),
                        AuditActionType.SYNC_SYSTEM.name()
                )))
                .toList();

        userRepository.saveAll(newUsers);
    }
}
