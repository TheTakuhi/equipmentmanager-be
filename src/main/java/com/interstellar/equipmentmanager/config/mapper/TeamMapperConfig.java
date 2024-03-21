package com.interstellar.equipmentmanager.config.mapper;

import com.interstellar.equipmentmanager.model.dto.team.in.TeamCreateDTO;
import com.interstellar.equipmentmanager.model.dto.team.in.TeamEditDTO;
import com.interstellar.equipmentmanager.model.dto.team.out.TeamCroppedDTO;
import com.interstellar.equipmentmanager.model.dto.team.out.TeamDTO;
import com.interstellar.equipmentmanager.model.dto.user.out.UserCroppedDTO;
import com.interstellar.equipmentmanager.model.dto.user.out.UserDTO;
import com.interstellar.equipmentmanager.model.entity.Team;

import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class TeamMapperConfig {

    private final ModelMapper mapper;

    private final Converter<Team, UUID> teamToId = ctx -> ctx.getSource() == null ? null : ctx.getSource().getId();

    private final Converter<UUID, Team> idToTeam = ctx -> {
        if (ctx.getSource() == null) {
            return null;
        }
        var team = new Team();
        team.setId(ctx.getSource());
        return team;
    };


    @Bean
    public void TeamConverting() {
        mapper.typeMap(Team.class, UUID.class).setConverter(teamToId);
        mapper.typeMap(UUID.class, Team.class).setConverter(idToTeam);
        mapper.typeMap(Team.class, TeamDTO.class).addMappings(
                em -> {
                    em.map(Team::getOwner, TeamDTO::setOwner);
                    em.map(Team::getMembers, TeamDTO::setMembers);
                }
        );
        mapper.typeMap(TeamDTO.class, Team.class).addMappings(
                em -> {
                    em.map(TeamDTO::getOwner, Team::setOwner);
                    em.map(TeamDTO::getMembers, Team::setMembers);
                }
        );
        mapper.typeMap(TeamEditDTO.class, Team.class).addMappings(
                em -> {
                    em.map(TeamEditDTO::getMembersIds, Team::setMembers);
                    em.map(TeamEditDTO::getOwnerId, Team::setOwner);
                }
        );
        mapper.typeMap(Team.class, TeamCroppedDTO.class).addMappings(
                em -> {
                    em.map(Team::getMembers, TeamCroppedDTO::setMembersIds);
                    em.map(Team::getOwner, TeamCroppedDTO::setOwnerId);
                }
        );

    }

}