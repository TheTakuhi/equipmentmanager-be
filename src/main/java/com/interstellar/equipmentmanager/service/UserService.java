package com.interstellar.equipmentmanager.service;

import com.interstellar.equipmentmanager.model.dto.UserDTO.UserCreateDTO;
import com.interstellar.equipmentmanager.model.dto.UserDTO.UserCroppedDTO;
import com.interstellar.equipmentmanager.model.dto.UserDTO.UserDTO;
import com.interstellar.equipmentmanager.model.dto.UserDTO.UserEditDTO;
import com.interstellar.equipmentmanager.model.entity.User;
import com.interstellar.equipmentmanager.model.filter.UserFilter;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

import java.util.UUID;

public interface UserService {
    UserDTO createUser(@NonNull UserCreateDTO userCreateDTO);

    UserDTO getUserById(@NonNull UUID id);

    UserDTO getUserByLdapId(@NonNull UUID ldapId);

    User getOriginalUser(@NonNull UUID id);

    Page<UserCroppedDTO> getAllUsers(@Nullable UserFilter filter, @NonNull Pageable pageable);

    UserDTO updateUser(@NonNull UUID id, @NonNull UserEditDTO userDTO, @NonNull Boolean syncRolesToKeycloak);

    void deleteUserById(@NonNull UUID id);
}
