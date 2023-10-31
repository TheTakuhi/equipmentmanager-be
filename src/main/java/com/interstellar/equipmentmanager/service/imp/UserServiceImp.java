package com.interstellar.equipmentmanager.service.imp;

import com.interstellar.equipmentmanager.exception.ResourceConflictException;
import com.interstellar.equipmentmanager.exception.ResourceNotFoundException;
import com.interstellar.equipmentmanager.model.dto.KeycloakUserDTO.KeycloakUserEditDTO;
import com.interstellar.equipmentmanager.model.dto.UserDTO.UserCreateDTO;
import com.interstellar.equipmentmanager.model.dto.UserDTO.UserCroppedDTO;
import com.interstellar.equipmentmanager.model.dto.UserDTO.UserDTO;
import com.interstellar.equipmentmanager.model.dto.UserDTO.UserEditDTO;
import com.interstellar.equipmentmanager.model.entity.User;
import com.interstellar.equipmentmanager.model.filter.UserFilter;
import com.interstellar.equipmentmanager.model.filter.UserSpecifications;
import com.interstellar.equipmentmanager.repository.UserRepository;
import com.interstellar.equipmentmanager.service.KeycloakService;
import com.interstellar.equipmentmanager.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImp implements UserService {
    private final ModelMapper mapper;
    private final UserRepository userRepository;
    private final KeycloakService keycloakService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public UserDTO createUser(@NonNull UserCreateDTO userCreateDTO) {
        if (userCreateDTO.getId() == null) {
            userCreateDTO.setId(UUID.randomUUID());
        }

        var user = mapper.map(userCreateDTO, User.class);
        requireValidUser(user);

        return mapper.map(userRepository.save(user), UserDTO.class);
    }

    private void requireValidUser(@NonNull User user) {
        if (user.getLdapId() == null) return;
        var problems = userRepository.findAllByLdapIdAndRemoved(user.getLdapId(), false)
                .stream().filter(u -> !u.getId().equals(user.getId()))
                .toList();

        if (!problems.isEmpty()) {
            var message = "User with ldpaId: [%s] intersects with users with id [%s]"
                    .formatted(
                            user.getLdapId(),
                            problems.stream().map(u -> u.getId().toString()).collect(Collectors.joining())
                    );
            throw new ResourceConflictException(message);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public UserDTO getUserById(@NonNull UUID id) {
        return mapper.map(getOriginalUser(id), UserDTO.class);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public UserDTO getUserByLdapId(@NonNull UUID ldapId) {
        var user = userRepository.findByLdapIdAndRemoved(ldapId, false)
                .orElseThrow(() ->
                        new ResourceNotFoundException(User.class.getName(), "ldapId", ldapId.toString()));
        return mapper.map(user, UserDTO.class);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public User getOriginalUser(@NonNull UUID id) {
        return userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(User.class.getName(), "id", id.toString()));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Page<UserCroppedDTO> getAllUsers(@Nullable UserFilter filter, @Nullable Pageable pageable) {
        if (pageable == null) pageable = Pageable.ofSize(Integer.MAX_VALUE);
        if (filter == null) filter = new UserFilter();

        Specification<User> spec = UserSpecifications.filterUsers(
                filter.getLogin() == null ? null : String.format("%%%s%%", filter.getLogin()),
                filter.getFullName() == null ? null : String.format("%%%s%%", filter.getFullName()),
                filter.getUserRoles(),
                filter.getIncludeRemovedUser() != null && filter.getIncludeRemovedUser()
        );

        var users = userRepository.findAll(spec, pageable);
        return users.map(u -> mapper.map(u, UserCroppedDTO.class));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public UserDTO updateUser(@NonNull UUID id, @NonNull UserEditDTO userEditDTO, @NonNull Boolean syncRolesToKeycloak) {
        var origUser = getOriginalUser(id);

        mapper.map(userEditDTO, origUser);
        origUser.setId(id);

        requireValidUser(origUser);
        var res = userRepository.save(origUser);

        if (syncRolesToKeycloak) {
            keycloakService.updateUserInKeycloak(
                    res.getLdapId(),
                    new KeycloakUserEditDTO(res.getUserRoles())
            );
        }

        return mapper.map(res, UserDTO.class);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteUserById(@NonNull UUID id) {
        var user = getOriginalUser(id);
        user.setRemoved(true);
        userRepository.save(user);
    }
}
