package com.interstellar.equipmentmanager.repository;

import com.interstellar.equipmentmanager.model.entity.User;
import com.interstellar.equipmentmanager.model.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {
    @Query("select distinct u from User u " +
            "left join u.userRoles urs where "
            + "(:includeRemoved = true or u.removed = false) and "
            + "(:loginPattern is null or lower(u.login) like lower(:loginPattern)) and "
            + "(:fullNamePattern is null or lower(u.fullName) like lower(:fullNamePattern)) and"
            + "((:userRoles) is null or urs in (:userRoles))")
    Page<User> findAll(@Param("loginPattern") String loginPattern,
                       @Param("fullNamePattern") @Nullable String fullNamePattern,
                       @Param("userRoles") @Nullable Collection<UserRole> userRoles,
                       @Param("includeRemoved") boolean includeRemoved,
                       Pageable pageable
    );

    Optional<User> findByLogin(@NonNull String login);

    Optional<User> findByIdAndRemoved(@NonNull UUID ldapId, boolean removed);

    List<User> findAllByEmailAndRemoved(@NonNull @NotBlank @Email String email, boolean removed);

    List<User> findAllByIdAndRemoved(@NonNull UUID ldapId, boolean removed);
}
