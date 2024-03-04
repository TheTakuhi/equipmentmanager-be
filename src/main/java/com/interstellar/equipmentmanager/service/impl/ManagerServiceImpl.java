package com.interstellar.equipmentmanager.service.impl;

import com.interstellar.equipmentmanager.exception.ResourceNotFoundException;
import com.interstellar.equipmentmanager.model.dto.user.out.UserDTO;
import com.interstellar.equipmentmanager.model.entity.Manager;
import com.interstellar.equipmentmanager.repository.ManagerRepository;
import com.interstellar.equipmentmanager.service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ManagerServiceImpl implements ManagerService {

    private final ManagerRepository managerRepository;
    private final ModelMapper mapper;

    @Override
    public List<Manager> saveAll(Iterable<Manager> managers) {
        return managerRepository.saveAll(managers);
    }

    @Override
    public Manager save(Manager manager) {
        return managerRepository.save(manager);
    }

    @Override
    public Manager findById(UUID id) {
        return managerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Manager not found"));
    }

    @Override
    public List<UserDTO> getManagedPeople(UUID id) {
        return this.findById(id).getManagedUsers().stream()
                .map((element) -> mapper.map(element, UserDTO.class))
                .toList();
    }
}
