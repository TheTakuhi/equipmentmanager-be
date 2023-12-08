package com.interstellar.equipmentmanager.repository;

import com.interstellar.equipmentmanager.model.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {
    Page<User> findAll(Specification<User> spec, Pageable pageable);
    Optional<User> findByLogin(@NonNull String login);
    Optional<User> findByIdAndRemoved(@NonNull UUID ldapId, boolean removed);
    List<User> findAllByEmailAndRemoved(@NonNull @NotBlank @Email String email, boolean removed);
    List<User> findAllByIdAndRemoved(@NonNull UUID ldapId, boolean removed);
}
