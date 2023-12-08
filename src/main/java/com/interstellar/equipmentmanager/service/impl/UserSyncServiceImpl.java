package com.interstellar.equipmentmanager.service.impl;

import com.interstellar.equipmentmanager.model.enums.UserRole;
import com.interstellar.equipmentmanager.model.dto.keycloak.user.out.KeycloakUserDTO;
import com.interstellar.equipmentmanager.model.dto.user.in.UserCreateDTO;
import com.interstellar.equipmentmanager.model.dto.user.out.UserDTO;
import com.interstellar.equipmentmanager.model.dto.user.in.UserEditDTO;
import com.interstellar.equipmentmanager.model.entity.AuditInfo;
import com.interstellar.equipmentmanager.model.entity.User;
import com.interstellar.equipmentmanager.model.filter.UserSpecifications;
import com.interstellar.equipmentmanager.repository.UserRepository;
import com.interstellar.equipmentmanager.service.UserService;
import com.interstellar.equipmentmanager.service.UserSyncService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserSyncServiceImpl implements UserSyncService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final ModelMapper mapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public @NonNull UserDTO syncUserFromKeycloak(@NonNull KeycloakUserDTO keycloakUser) {
        Optional<User> userOpt = keycloakUser.getLdapId() == null ?
                Optional.empty() :
                userRepository.findByIdAndRemoved(keycloakUser.getLdapId(), false);

        if (userOpt.isEmpty() && keycloakUser.getEmail() != null)
        {
            var users = userRepository.findAllByEmailAndRemoved(keycloakUser.getEmail(), false);
            if (users.size() == 1) userOpt = Optional.of(users.get(0));
        }

        if (userOpt.isEmpty()) {
            Specification<User> spec = UserSpecifications.filterUsers(
                    keycloakUser.getLogin() != null ? String.format("%%%s%%", keycloakUser.getLogin()) : null,
                    null,
                    new ArrayList<>(),
                    false
            );

            Sort sort = Sort.by(Sort.Order.desc("auditInfo.lastModifiedAt"));

            var users = userRepository.findAll(spec, sort);
            Optional<User> userOptional = users.stream().findFirst();

            if (userOptional.isPresent()) {
                userOpt = Optional.of(userOptional.get());
            }
        }

        if (userOpt.isPresent()) {
            var user = userOpt.get();
            var auditInfo = user.getAuditInfo() == null ?
                    new AuditInfo() :
                    user.getAuditInfo();

            if (auditInfo.getLastModifiedAt() == null)
                auditInfo.setLastModifiedAt(Instant.now());

            var origRoles = new ArrayList<>(user.getUserRoles());
            origRoles.sort(Comparator.comparing(UserRole::getPower));
            keycloakUser.getUserRoles().sort(Comparator.comparing(UserRole::getPower));

            if (keycloakUser.getUserRoles().equals(origRoles) &&
                    Instant.now().toEpochMilli() - auditInfo.getLastModifiedAt().toEpochMilli() < 60_000) {
                return mapper.map(user, UserDTO.class);
            }

            log.info("Updating user with userId: {}", userOpt.get().getId());
            var editDTO = mapper.map(keycloakUser, UserEditDTO.class);

            auditInfo.setLastModifiedBy("SYNC_SYSTEM");
            auditInfo.setLastModifiedAt(Instant.now());

            editDTO.setAuditInfo(auditInfo);

            return userService.updateUser(userOpt.get().getId(), editDTO, false);
        } else {
            log.info("Failed to find user [{}] in system", keycloakUser);
            var createDTO = mapper.map(keycloakUser, UserCreateDTO.class);
            log.info("Creating user {}", createDTO);
            return userService.createUser(createDTO);
        }
    }
}
