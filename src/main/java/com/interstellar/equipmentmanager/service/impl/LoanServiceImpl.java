package com.interstellar.equipmentmanager.service.impl;

import com.interstellar.equipmentmanager.exception.ForbiddenActionException;
import com.interstellar.equipmentmanager.exception.ResourceConflictException;
import com.interstellar.equipmentmanager.exception.ResourceNotFoundException;
import com.interstellar.equipmentmanager.model.dto.loan.in.LoanCreateDTO;
import com.interstellar.equipmentmanager.model.dto.loan.out.LoanDTO;
import com.interstellar.equipmentmanager.model.entity.Item;
import com.interstellar.equipmentmanager.model.entity.Loan;
import com.interstellar.equipmentmanager.model.entity.Team;
import com.interstellar.equipmentmanager.model.entity.User;
import com.interstellar.equipmentmanager.model.enums.State;
import com.interstellar.equipmentmanager.model.enums.UserRole;
import com.interstellar.equipmentmanager.model.filter.LoanFilter;
import com.interstellar.equipmentmanager.model.filter.LoanSpecifications;
import com.interstellar.equipmentmanager.repository.LoanRepository;
import com.interstellar.equipmentmanager.security.service.UserAuthorizationService;
import com.interstellar.equipmentmanager.service.ItemService;
import com.interstellar.equipmentmanager.service.LoanService;
import com.interstellar.equipmentmanager.service.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
@AllArgsConstructor
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final UserService userService;
    private final ItemService itemService;
    private final ModelMapper mapper;
    private final UserAuthorizationService userAuthorizationService;

    @Override
    public LoanDTO createLoan(LoanCreateDTO loanCreateDTO) {
        Loan loan = mapper.map(loanCreateDTO, Loan.class);

        User currentUser = userService.getOriginalUser(userAuthorizationService.getCurrentUser().getId());
        Item item = itemService.getItemById(loan.getItem().getId());

        if (canUserLendItem(currentUser, item) || item.getOwner().equals(currentUser) || currentUser.getUserRoles().contains(UserRole.ADMIN)) {
            loan.setBorrower(userService.getOriginalUser(loan.getBorrower().getId()));
            loan.setLender(mapper.map(userAuthorizationService.getCurrentUser(), User.class));


            if (item.getState().equals(State.AVAILABLE)) {
                loan.setItem(item);
            } else {
                throw new ResourceConflictException(String.format("Item with id %s cannot be lend because it is already %s ", item.getId().toString(), item.getState()));
            }
        } else
            throw new ForbiddenActionException(String.format("You have not permission to lend item with id %s.", item.getId()));

        return mapper.map(loanRepository.save(loan), LoanDTO.class);
    }

    private boolean canUserLendItem(User user, Item item) {
        for (Team team : user.getTeams()) {
            if (team.getMembers().contains(user) && team.getMembers().contains(item.getOwner())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public LoanDTO getLoanDTOById(UUID id) {
        Loan loan = loanRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Loan.class.getName(), "id", id.toString()));
        return mapper.map(loan, LoanDTO.class);
    }


    public Loan getLoanById(UUID id) {
        Loan loan = loanRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Loan.class.getName(), "id", id.toString()));
        return loan;
    }

    @Override
    public LoanDTO returnLoan(UUID id, LocalDate date) {
        Loan loan = getLoanById(id);
        loan.getItem().setState(State.AVAILABLE);
        loan.setReturnDate(date);
        return mapper.map(loanRepository.save(loan), LoanDTO.class);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Page<LoanDTO> getAllLoans(@Nullable LoanFilter filter, @Nullable Pageable pageable) {
        if (pageable == null) pageable = Pageable.ofSize(Integer.MAX_VALUE);
        if (filter == null) filter = new LoanFilter();


        Specification<Loan> spec = LoanSpecifications.filterLoans(
                filter.getSerialCode() == null ? null : String.format("%%%s%%", filter.getSerialCode()),
                filter.getType(),
                filter.getBorrowerName() == null ? null : String.format("%%%s%%", filter.getBorrowerName()),
                filter.getLenderName() == null ? null : String.format("%%%s%%", filter.getLenderName()),
                userAuthorizationService.getCurrentUser().getUserRoles().contains(UserRole.ADMIN) ? null : userAuthorizationService.getCurrentUser().getId()
        );

        var loans = loanRepository.findAll(spec, pageable);
        return loans.map(l -> mapper.map(l, LoanDTO.class));
    }


}
