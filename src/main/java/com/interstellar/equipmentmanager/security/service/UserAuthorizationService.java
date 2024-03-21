package com.interstellar.equipmentmanager.security.service;

import com.interstellar.equipmentmanager.model.dto.user.out.CurrentUserDTO;
import com.interstellar.equipmentmanager.model.dto.user.out.UserCroppedDTO;
import com.interstellar.equipmentmanager.model.dto.user.out.UserDTO;
import com.interstellar.equipmentmanager.model.enums.UserRole;
import lombok.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;

import java.util.UUID;

public interface UserAuthorizationService {

    @Nullable
    UserCroppedDTO getCurrentUserCropped();

    @Nullable
    UserDTO getCurrentUser();

    @Nullable
    UserDTO getCurrentUser(@NonNull Authentication authentication);

    @Nullable
    CurrentUserDTO getUserClaims(@NonNull Authentication authentication);

    @NonNull
    Boolean hasMinimalRole(@Nullable String role);

    @NonNull
    Boolean hasMinimalRole(@NonNull UserRole role);

    @NonNull
    Boolean hasId(@Nullable UUID id);

    @NonNull
    Boolean hasLdapID(@Nullable UUID ldapId);
}
