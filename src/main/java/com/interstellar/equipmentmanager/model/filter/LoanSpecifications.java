package com.interstellar.equipmentmanager.model.filter;

import com.interstellar.equipmentmanager.model.entity.Item;
import com.interstellar.equipmentmanager.model.entity.Loan;
import com.interstellar.equipmentmanager.model.entity.User;
import com.interstellar.equipmentmanager.model.enums.Type;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class LoanSpecifications {
    public static Specification<Loan> filterLoans(String serialCode, Type type, String borrowerName, String lenderName, UUID requesterUserId) {
        return (Root<Loan> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            Join<Loan, Item> item = root.join("item");
            Join<Loan, User> borrower = root.join("borrower");
            Join<Loan, User> lender = root.join("lender");
            if (serialCode != null) {
                predicates.add(builder.like(item.get("serialCode"), serialCode));
            }

            if (type != null) {
                predicates.add(builder.equal(item.get("type"), type));
            }
            if (borrowerName != null) {
                predicates.add(builder.or(builder.like(borrower.get("login"), borrowerName), builder.like(borrower.get("fullName"), borrowerName)));
            }
            if (lenderName != null) {
                predicates.add(builder.or(builder.like(lender.get("login"), lenderName), builder.like(lender.get("fullName"), lenderName)));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
