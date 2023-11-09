package com.interstellar.equipmentmanager.service;

import com.interstellar.equipmentmanager.model.dto.request.LdapUser;
import lombok.NonNull;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ReactiveLdapQLService {
    @NonNull Mono<List<LdapUser>> findUserByEmail(@NonNull String email);
}
