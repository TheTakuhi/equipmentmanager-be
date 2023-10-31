package com.interstellar.equipmentmanager.security.service;

import com.interstellar.equipmentmanager.model.dto.UserDTO.UserCroppedDTO;
import com.interstellar.equipmentmanager.model.dto.UserDTO.UserDTO;
import com.interstellar.equipmentmanager.model.dto.UserDTO.UserEditDTO;
import com.interstellar.equipmentmanager.model.enums.UserRole;
import lombok.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;

import java.util.UUID;

public interface UserAuthorizationService {
    @Nullable
    UserCroppedDTO getCurrentUser();

    @Nullable
    UserCroppedDTO getCurrentUser(@NonNull Authentication authentication);
    @Nullable
    UserDTO syncUserFromAuth(@NonNull Authentication authentication);
    @Nullable
    UserEditDTO getUserClaims(@NonNull Authentication authentication);

    @NonNull
    Boolean hasMinimalRole(@Nullable String role);
    @NonNull
    Boolean hasMinimalRole(@NonNull UserRole role);
    @NonNull
    Boolean hasId(@Nullable UUID id);
    @NonNull
    Boolean hasLdapID(@Nullable UUID ldapId);
}
