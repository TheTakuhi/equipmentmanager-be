package com.interstellar.equipmentmanager.service;

import com.interstellar.equipmentmanager.model.dto.response.LdapUser;
import com.interstellar.equipmentmanager.model.entity.User;
import lombok.NonNull;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ReactiveLdapQLService {
    @NonNull Mono<List<LdapUser>> findUserByEmail(@NonNull String email);

    Mono<List<LdapUser>> findAllUsers();

    Mono<List<LdapUser>> findAllUsersNotIn(List<User> users);
}
