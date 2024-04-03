package com.interstellar.equipmentmanager.repository;

import com.interstellar.equipmentmanager.model.entity.Team;
import com.interstellar.equipmentmanager.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TeamRepository extends JpaRepository<Team, UUID>, JpaSpecificationExecutor<Team> {
    
    Page<Team> findAll(Pageable pageable);
    
    Page<Team> findByMembersId(UUID id, Pageable pageable);
    
    @Query("""
        SELECT members
        FROM Team t left join t.members members
        WHERE t.id = :id AND (
            lower(members.login) LIKE  %:search% OR lower(members.fullName) LIKE %:search%
        )
    """)
    Page<User> searchMembersByTeamId(UUID id, String search, Pageable pageable);
}
