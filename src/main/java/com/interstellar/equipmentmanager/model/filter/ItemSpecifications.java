package com.interstellar.equipmentmanager.model.filter;

import com.interstellar.equipmentmanager.model.entity.Item;
import com.interstellar.equipmentmanager.model.entity.Team;
import com.interstellar.equipmentmanager.model.entity.User;
import com.interstellar.equipmentmanager.model.enums.QualityState;
import com.interstellar.equipmentmanager.model.enums.State;
import com.interstellar.equipmentmanager.model.enums.Type;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class ItemSpecifications {

    public static Specification<Item> filterItems(String serialCodePattern, Type type, State state, QualityState qualityState,Boolean includeDiscarded, UUID userId) {
        return (Root<Item> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (serialCodePattern != null) {
                predicates.add(builder.like(root.get("serialCode"), serialCodePattern));
            }
            if (type != null) {
                predicates.add(builder.equal(root.get("type"), type));
            }
            if (state != null) {
                predicates.add(builder.equal(root.get("state"), state));
            }
            if (qualityState != null) {
                predicates.add(builder.equal(root.get("qualityState"), qualityState));
            }

            if (!includeDiscarded) {
                Predicate notDiscardedPredicate = builder.notEqual(root.get("state"), State.DISCARDED);
                predicates.add(notDiscardedPredicate);
            }

            if (userId != null) {
                Join<Item, User> itemOwnerJoin = root.join("owner");

                Predicate userIsOwnerPredicate = builder.equal(itemOwnerJoin.get("id"), userId);

                Join<User, Team> ownerTeamJoin = itemOwnerJoin.join("teams");
                Subquery<Team> teamSubquery = query.subquery(Team.class);
                Root<User> teamUserRoot = teamSubquery.from(User.class);
                Join<User, Team> userTeamJoin = teamUserRoot.join("teams");

                teamSubquery.select(userTeamJoin)
                        .where(builder.equal(teamUserRoot.get("id"), userId));

                Predicate userInSameTeamPredicate = builder.in(ownerTeamJoin).value(teamSubquery);
                predicates.add(builder.or(userIsOwnerPredicate, userInSameTeamPredicate));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };

    }
}