package com.interstellar.equipmentmanager.service;

import com.interstellar.equipmentmanager.model.dto.user.out.UserDTO;
import com.interstellar.equipmentmanager.model.entity.Manager;

import java.util.List;
import java.util.UUID;

public interface ManagerService {
    List<Manager> saveAll(Iterable<Manager> managers);

    Manager save(Manager manager);

    Manager findById(UUID id);

    List<UserDTO> getManagedPeople(UUID id);
}
