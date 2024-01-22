package com.interstellar.equipmentmanager.service;

import com.interstellar.equipmentmanager.model.dto.loan.in.LoanCreateDTO;
import com.interstellar.equipmentmanager.model.dto.loan.out.LoanDTO;
import com.interstellar.equipmentmanager.model.entity.Loan;
import com.interstellar.equipmentmanager.model.filter.LoanFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

public interface LoanService {
    LoanDTO createLoan(LoanCreateDTO loanCreateDTO);

    LoanDTO getLoanDTOById(UUID id);

    Loan getLoanById(UUID id);

    LoanDTO returnLoan(UUID id, LocalDate date);

    @Transactional(propagation = Propagation.REQUIRED)
    Page<LoanDTO> getAllLoans(@Nullable LoanFilter filter, @Nullable Pageable pageable);
}
