package com.interstellar.equipmentmanager.service;

import com.interstellar.equipmentmanager.model.entity.LdapUser;
import lombok.NonNull;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ReactiveLdapQLService {
    @NonNull Mono<List<LdapUser>> findUserByEmail(@NonNull String email);
}
