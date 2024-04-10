package com.interstellar.equipmentmanager.repository;

import com.interstellar.equipmentmanager.model.entity.Team;
import com.interstellar.equipmentmanager.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TeamRepository extends JpaRepository<Team, UUID>, JpaSpecificationExecutor<Team> {
    
    Page<Team> findAll(Pageable pageable);
    
    @Query("""
        SELECT members
        FROM Team t left join t.members members
        WHERE t.id = :id AND (
            lower(members.login) LIKE %:search% OR lower(members.fullName) LIKE %:search%
        )
    """)
    Page<User> searchMembersByTeamId(UUID id, String search, Pageable pageable);
    
    @Query("""
        SELECT distinct team
        FROM Team team join team.members members
        WHERE members.id = :id
            AND lower(team.teamName) LIKE %:search%
            OR lower(members.fullName) LIKE %:search%
            OR lower(members.login) LIKE %:search%
    """)
    Page<Team> findByMembersIdAndSearch(@Param("id") UUID id, @Param("search") String search, Pageable pageable);
    
    @Query("""
        SELECT distinct team
        FROM Team team join team.members members
        WHERE lower(team.teamName) LIKE %:search%
            OR lower(members.fullName) LIKE %:search%
            OR lower(members.login) LIKE %:search%
    """)
    Page<Team> findTeamsWithSearch(@Param("search") String search, Pageable pageable);
    
    boolean existsByTeamName(String teamName);
    
    boolean existsByTeamNameAndIdIsNot(String teamName, UUID id);
}
