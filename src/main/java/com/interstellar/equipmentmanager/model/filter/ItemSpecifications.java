package com.interstellar.equipmentmanager.model.filter;

import com.interstellar.equipmentmanager.model.entity.Item;
import com.interstellar.equipmentmanager.model.enums.QualityState;
import com.interstellar.equipmentmanager.model.enums.State;
import com.interstellar.equipmentmanager.model.enums.Type;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ItemSpecifications {
    public static Specification<Item> filterItems(String serialCodePattern, Type type, State state, QualityState qualityState) {
        return (Root<Item> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (serialCodePattern != null) {
                predicates.add(builder.like(root.get("serialCode"), serialCodePattern));
            }

            // Assuming type, state, and qualityState are attributes in Item class
            if (type != null) {
                predicates.add(builder.equal(root.get("type"), type));
            }

            if (state != null) {
                predicates.add(builder.equal(root.get("state"), state));
            }

            if (qualityState != null) {
                predicates.add(builder.equal(root.get("qualityState"), qualityState));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}