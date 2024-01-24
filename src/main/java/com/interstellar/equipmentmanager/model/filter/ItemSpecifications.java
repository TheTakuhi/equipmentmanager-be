package com.interstellar.equipmentmanager.model.filter;

import com.interstellar.equipmentmanager.model.entity.Item;
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

            return builder.and(predicates.toArray(new Predicate[0]));
        };

    }
}