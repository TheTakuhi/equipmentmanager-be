package com.interstellar.equipmentmanager.model.filter;

import com.interstellar.equipmentmanager.model.entity.User;
import com.interstellar.equipmentmanager.model.enums.UserRole;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserSpecifications {
    public static Specification<User> filterUsers(String loginPattern, String fullNamePattern, Collection<UserRole> userRoles, boolean includeRemoved) {
        return (Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (loginPattern != null) {
                predicates.add(builder.like(root.get("login"), loginPattern));
            }

            if (fullNamePattern != null) {
                predicates.add(builder.like(root.get("fullName"), fullNamePattern));
            }

            if (userRoles != null && !userRoles.isEmpty()) {
                predicates.add(root.get("userRoles").in(userRoles));
            }

            if (!includeRemoved) {
                predicates.add(builder.isFalse(root.get("removed")));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}