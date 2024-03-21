package com.interstellar.equipmentmanager.model.filter;

import com.interstellar.equipmentmanager.model.entity.Item;
import com.interstellar.equipmentmanager.model.entity.Loan;
import com.interstellar.equipmentmanager.model.entity.Team;
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

            if (requesterUserId != null) {
                Join<Item, User> itemOwnerJoin = item.join("owner");

                Predicate requesterIsOwnerPredicate = builder.equal(itemOwnerJoin.get("id"), requesterUserId);

                Join<User, Team> ownerTeamJoin = itemOwnerJoin.join("teams");
                Subquery<Team> teamSubquery = query.subquery(Team.class);
                Root<User> teamUserRoot = teamSubquery.from(User.class);
                Join<User, Team> userTeamJoin = teamUserRoot.join("teams");

                teamSubquery.select(userTeamJoin)
                        .where(builder.equal(teamUserRoot.get("id"), requesterUserId));

                Predicate requesterInSameTeamPredicate = builder.in(ownerTeamJoin).value(teamSubquery);
                predicates.add(builder.or(requesterIsOwnerPredicate, requesterInSameTeamPredicate));
            }


            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}